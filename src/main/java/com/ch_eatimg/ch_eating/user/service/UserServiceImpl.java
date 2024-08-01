package com.ch_eatimg.ch_eating.user.service;

import com.ch_eatimg.ch_eating.domain.*;
import com.ch_eatimg.ch_eating.exception.TokenExpiredException;
import com.ch_eatimg.ch_eating.role.RoleRepository;
import com.ch_eatimg.ch_eating.security.JwtTokenProvider;
import com.ch_eatimg.ch_eating.user.dto.*;
import com.ch_eatimg.ch_eating.user.repository.UserRepository;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import com.ch_eatimg.ch_eating.util.vaild.CustomValid;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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

        // 비밀번호 확인
        if (!user.getUserPassword().equals(dto.getUserPassword())) {
            return CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "비밀번호를 확인하세요.");
        }

        List<Role> roles = user.getUserRoles().stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());

        String accessToken = jwtTokenProvider.createToken(user.getUserId(), roles);
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), roles);

        // 쿠키에 Refresh Token 설정
        int maxAge = (int) (jwtTokenProvider.getRefreshTokenValidityInMilliseconds() / 1000);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // HTTPS를 사용하는 경우
                .path("/")
                .maxAge(maxAge)
                .sameSite("none")
                .build();
        response.setHeader("Set-Cookie", cookie.toString());

        return CustomApiResponse.createSuccess(
                HttpStatus.OK.value(),
                UserSignInResDto.builder().accessToken(accessToken).build(),
                "로그인 성공"
        );
    }


    @Override
    public UserInfoResDto getUserInfo(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        User user = null;
        String newAccessToken = null;

        try {
            // 1. 액세스 토큰이 유효한 경우
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                String username = jwtTokenProvider.getUsername(accessToken);
                user = userRepository.findByUserId(username)
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            } else {
                // 2. 액세스 토큰이 유효하지 않은 경우 리프레쉬 토큰 확인
                String refreshToken = getRefreshTokenFromCookies(request);
                if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                    // 리프레쉬 토큰이 유효한 경우 새로운 액세스 토큰 발급
                    String username = jwtTokenProvider.getUsername(refreshToken);
                    user = userRepository.findByUserId(username)
                            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

                    newAccessToken = jwtTokenProvider.createToken(
                            username,
                            user.getUserRoles().stream()
                                    .map(UserRole::getRole)
                                    .collect(Collectors.toList())
                    );
                } else {
                    throw new TokenExpiredException("리프레쉬 토큰이 만료되었습니다.");
                }
            }

            if (user == null) {
                throw new TokenExpiredException("로그인 상태가 아닙니다.");
            }

            return UserInfoResDto.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .accessToken(newAccessToken) // 새로운 액세스 토큰이 있을 경우 설정
                    .build();
        } catch (TokenExpiredException e) {
            throw new TokenExpiredException("로그인 상태가 아닙니다.");
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("잘못된 토큰입니다.", e); // 추가된 부분: 잘못된 토큰 처리
        }
    }

    @Transactional
    @Override
    public CustomApiResponse<String> deleteUser(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        String refreshToken = getRefreshTokenFromCookies(request);

        try {
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                handleUserDeletion(accessToken, refreshToken, "유효한 액세스 토큰이 존재합니다.");
            } else if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
                handleUserDeletion(refreshToken, refreshToken, "액세스 토큰이 만료되었지만 유효한 리프레쉬 토큰이 존재합니다.");
            } else {
                return CustomApiResponse.createFailWithout(HttpStatus.UNAUTHORIZED.value(), "유효한 토큰이 없거나 모두 만료되었습니다. 로그인 후 다시 시도해주세요.");
            }

            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), "회원 탈퇴가 완료되었습니다.", "회원 탈퇴 성공");
        } catch (RuntimeException e) {
            return CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "회원 탈퇴 중 문제가 발생했습니다: " + e.getMessage());
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
    public CustomApiResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromCookies(request);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        if (refreshToken != null) {
            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), "로그아웃 되었습니다.", "로그아웃 성공");
        } else {
            return CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "로그아웃 요청에서 유효한 리프레쉬 토큰을 찾을 수 없습니다.");
        }
    }

    @Override
    public CustomApiResponse<String> checkUserIdExists(String userId) {
        boolean exists = userRepository.findByUserId(userId).isPresent();
        String message = exists ? "중복된 아이디입니다." : "사용 가능한 아이디입니다.";
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), message, "아이디 중복 확인");
    }

    public CustomApiResponse<UserMyPageResDto> getMyPage(HttpServletRequest request) {
        try {
            String accessToken = jwtTokenProvider.resolveToken(request);
            if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
                throw new RuntimeException("유효한 액세스 토큰이 없습니다.");
            }

            String userId = jwtTokenProvider.getUsername(accessToken);
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            UserMyPageResDto userMyPageResDto = UserMyPageResDto.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .build();

            return CustomApiResponse.createSuccess(
                    HttpStatus.OK.value(),
                    userMyPageResDto,
                    "마이페이지 정보 조회 성공"
            );
        } catch (RuntimeException e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.UNAUTHORIZED.value(),
                    "마이페이지 정보 조회 실패: " + e.getMessage()
            );
        }
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
