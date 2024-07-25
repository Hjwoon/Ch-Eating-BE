package com.ch_eatimg.ch_eating.domain;

import com.ch_eatimg.ch_eating.user.dto.UserSignUpReqDto;
import com.ch_eatimg.ch_eating.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "USER_ID", //식별자 생성기 이름
        sequenceName = "USER_ID_SEQ", //DB에 등록되어 있는 Sequence 이름
        initialValue = 1, //처음 시작 value 설정
        allocationSize = 1 //Sequence 한번 호출 시 증가하는 수
        //allocationSize가 기본값이 50이므로 1로 설정하지 않을 시, sequence 호출 시 마다 50씩 증가
)
@Table(name = "USERS")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "id"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_phone", nullable = false, unique = true)
    private String userPhone;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserRole> userRoles;

    public static User toEntity(UserSignUpReqDto dto, List<Role> roles) {

        User user = User.builder()
                .userId(dto.getUserId())
                .userPassword(dto.getUserPassword())
                .userName(dto.getUserName())
                .userPhone(dto.getUserPhone())
                .build();

        List<UserRole> userRoles = dto.getUserRoles().isEmpty()
                ? List.of(findRole(roles, "ROLE_CLIENT")) // Default to 'role_client' if no roles are provided
                : dto.getUserRoles().stream()
                .map(roleName -> findRole(roles, roleName))
                .collect(Collectors.toList());

        user.userRoles = userRoles;

        return user;
    }

    private static UserRole findRole(List<Role> roles, String roleName) {
        return roles.stream()
                .filter(role -> role.getRoleName().name().equals(roleName))
                .findFirst()
                .map(role -> new UserRole(null, null, role))
                .orElseThrow(() -> new IllegalArgumentException(roleName + "는 존재하지 않는 역할입니다."));
    }
}