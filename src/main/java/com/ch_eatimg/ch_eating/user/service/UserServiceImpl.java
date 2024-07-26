package com.ch_eatimg.ch_eating.user.service;

import com.ch_eatimg.ch_eating.domain.RoleName;
import com.ch_eatimg.ch_eating.domain.User;
import com.ch_eatimg.ch_eating.domain.UserRole;
import com.ch_eatimg.ch_eating.role.RoleRepository;
import com.ch_eatimg.ch_eating.security.JwtTokenProvider;
import com.ch_eatimg.ch_eating.user.dto.UserSignInReqDto;
import com.ch_eatimg.ch_eating.user.dto.UserSignInResDto;
import com.ch_eatimg.ch_eating.user.dto.UserSignUpReqDto;
import com.ch_eatimg.ch_eating.user.repository.UserRepository;
import com.ch_eatimg.ch_eating.domain.Role;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Role defaultRole = roleRepository.findByRoleName(RoleName.ROLE_CLIENT)
                .orElseThrow(() -> new IllegalArgumentException("기본 역할이 설정되어 있지 않습니다."));

        User user = User.toEntity(dto, defaultRole);
        userRepository.save(user);

        return "회원가입에 성공했습니다.";
    }

    @Override
    public UserSignInResDto signIn(UserSignInReqDto dto) {
        User user = userRepository.findByUserId(dto.getUserId()).orElseThrow(RuntimeException::new);
        List<Role> roles = user.getUserRoles().stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());

        String token = jwtTokenProvider.createToken(user.getUserId(), roles);
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        return UserSignInResDto.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }
}
