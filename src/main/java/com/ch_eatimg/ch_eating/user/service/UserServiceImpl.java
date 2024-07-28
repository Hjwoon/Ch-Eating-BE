package com.ch_eatimg.ch_eating.user.service;

import com.ch_eatimg.ch_eating.domain.*;
import com.ch_eatimg.ch_eating.exception.TokenExpiredException;
import com.ch_eatimg.ch_eating.role.RoleRepository;
import com.ch_eatimg.ch_eating.security.JwtTokenProvider;
import com.ch_eatimg.ch_eating.user.dto.*;
import com.ch_eatimg.ch_eating.user.repository.UserRepository;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import com.ch_eatimg.ch_eating.util.vaild.CustomValid;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public CustomApiResponse<UserSignUpResDto> signUp(UserSignUpReqDto dto, HttpServletResponse response) {
        Optional<User> optionalUser = userRepository.findByUserId(dto.getUserId());
        if (optionalUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        List<Role> roles = dto.getUserRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(RoleName.valueOf(roleName))
                        .orElseThrow(() -> new IllegalArgumentException(roleName + "은 존재하지 않는 역할입니다.")))
                .collect(Collectors.toList());

        User user = User.toEntity(dto, roles);
        userRepository.save(user);

        UserSignUpResDto result = new UserSignUpResDto("회원가입이 성공적으로 완료되었습니다.", user.getId().toString());
        return CustomApiResponse.createSuccess(HttpStatus.CREATED.value(), result, "회원가입 성공");
    }

    @Override
    public CustomApiResponse<UserSignInResDto> signIn(UserSignInReqDto dto, HttpServletResponse response) {
        // 아이디와 비밀번호 유효성 검증
        if (dto.getUserId() == null || dto.getUserId().isEmpty() || dto.getUserPassword() == null || dto.getUserPassword().isEmpty()) {
            return CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "아이디와 비밀번호는 필수 입력 사항입니다.");
        }

        if (!CustomValid.isUserIdValid(dto.getUserId())) {
            return CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "아이디는 6~12자의 영문자, 숫자, 하이픈, 언더스코어만 사용할 수 있습니다.");
        }

        User user;
        try {
            user = userRepository.findByUserId(dto.getUserId()).orElseThrow(() -> new RuntimeException("존재하지 않는 아이디 입니다. 아이디를 확인해주세요"));
        } catch (RuntimeException e) {
            return CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

        // 비밀번호 확인 (비밀번호는 예시를 위해 단순 문자열 비교로 작성)
        if (!user.getUserPassword().equals(dto.getUserPassword())) {
            return CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "비밀번호를 확인하세요.");
        }

        List<Role> roles = user.getUserRoles().stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());

        String accessToken = jwtTokenProvider.createToken(user.getUserId(), roles);
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), roles);

        // 쿠키에 Refresh Token 설정
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 1주일
        response.addCookie(cookie);

        return CustomApiResponse.createSuccess(
                HttpStatus.OK.value(),
                UserSignInResDto.builder().accessToken(accessToken).build(),
                "로그인 성공"
        );
    }




    @Transactional(readOnly = true)
    @Override
    public UserInfoDto getUserInfo(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);

        try {
            // 1. 액세스 토큰이 유효한 경우
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                String username = jwtTokenProvider.getUsername(accessToken);
                User user = userRepository.findByUserId(username)
                        .orElseThrow(() -> new RuntimeException());

                return UserInfoDto.builder()
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .build();
            } else {
                throw new TokenExpiredException("액세스 토큰이 만료되었습니다.");
            }
        } catch (TokenExpiredException e) {
            // 2. 액세스 토큰이 유효하지 않은 경우 리프레쉬 토큰 확인
            String refreshToken = getRefreshTokenFromCookies(request);

            try {
                if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                    // 리프레쉬 토큰이 유효한 경우 새로운 액세스 토큰 발급
                    String newAccessToken = jwtTokenProvider.createToken(
                            jwtTokenProvider.getUsername(refreshToken),
                            userRepository.findByUserId(jwtTokenProvider.getUsername(refreshToken))
                                    .orElseThrow()
                                    .getUserRoles().stream()
                                    .map(UserRole::getRole)
                                    .collect(Collectors.toList())
                    );

                    // 사용자 정보를 반환
                    User user = userRepository.findByUserId(jwtTokenProvider.getUsername(refreshToken))
                            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

                    return UserInfoDto.builder()
                            .userId(user.getUserId())
                            .userName(user.getUserName())
                            .accessToken(newAccessToken)
                            .build();
                } else {
                    throw new TokenExpiredException("리프레쉬 토큰이 만료되었습니다.");
                }
            } catch (TokenExpiredException innerException) {
                // 3. 리프레쉬 토큰도 유효하지 않은 경우
                throw new TokenExpiredException("로그인 상태가 아닙니다.");
            }
        }
    }

    @Transactional
    @Override
    public void deleteUser(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        String refreshToken = getRefreshTokenFromCookies(request);

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            handleUserDeletion(accessToken, refreshToken, "유효한 액세스 토큰이 존재합니다.");
        } else if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            handleUserDeletion(refreshToken, refreshToken, "액세스 토큰이 만료되었지만 유효한 리프레쉬 토큰이 존재합니다.");
        } else {
            throw new RuntimeException("유효한 토큰이 없거나 모두 만료되었습니다. 로그인 후 다시 시도해주세요.");
        }
    }

    private void handleUserDeletion(String token, String refreshToken, String message) {
        String username = jwtTokenProvider.getUsername(token);
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        userRepository.delete(user);

        System.out.println("사용자 " + username + " 삭제 완료. " + message);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromCookies(request);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        if (refreshToken != null) {
            System.out.println("리프레쉬 토큰이 무효화되었습니다.");
        } else {
            System.out.println("로그아웃 요청에서 유효한 리프레쉬 토큰을 찾을 수 없습니다.");
        }
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
