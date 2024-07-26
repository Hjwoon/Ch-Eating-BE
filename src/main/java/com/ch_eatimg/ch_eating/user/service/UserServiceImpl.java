package com.ch_eatimg.ch_eating.user.service;

import com.ch_eatimg.ch_eating.domain.*;
import com.ch_eatimg.ch_eating.role.RoleRepository;
import com.ch_eatimg.ch_eating.security.JwtTokenProvider;
import com.ch_eatimg.ch_eating.user.dto.UserSignInReqDto;
import com.ch_eatimg.ch_eating.user.dto.UserSignInResDto;
import com.ch_eatimg.ch_eating.user.dto.UserSignUpReqDto;
import com.ch_eatimg.ch_eating.user.repository.UserRepository;
import com.ch_eatimg.ch_eating.domain.Role;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletResponse;

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
    public String signUp(UserSignUpReqDto dto) {
        Optional<User> optionalUser = userRepository.findByUserId(dto.getUserId());
        if (optionalUser.isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        List<Role> roles = dto.getUserRoles().stream()
                .map(roleName -> roleRepository.findByRoleName(RoleName.valueOf(roleName))
                        .orElseThrow(() -> new IllegalArgumentException(roleName + "은 존재하지 않는 역할입니다.")))
                .collect(Collectors.toList());

        User user = User.toEntity(dto, roles);
        userRepository.save(user);

        // 디버깅용 코드
        user.getUserRoles().forEach(userRole -> System.out.println("Saved role for user: " + userRole.getRole().getRoleName()));

        return "회원가입에 성공했습니다.";
    }

    @Override
    public UserSignInResDto signIn(UserSignInReqDto dto, HttpServletResponse response) {
        User user = userRepository.findByUserId(dto.getUserId()).orElseThrow(RuntimeException::new);
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

        return UserSignInResDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
