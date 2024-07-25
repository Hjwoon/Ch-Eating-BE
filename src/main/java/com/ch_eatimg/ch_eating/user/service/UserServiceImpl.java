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
    public UserSignInResDto signIn(UserSignInReqDto dto) {
        User user = userRepository.findByUserId(dto.getUserId()).orElseThrow(RuntimeException::new);
        List<Role> roles = user.getUserRoles().stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());

        String token = jwtTokenProvider.createToken(user.getUserId(), roles);
        return UserSignInResDto.builder().accessToken(token).build();
    }
}
