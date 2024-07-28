package com.ch_eatimg.ch_eating.domain;

import com.ch_eatimg.ch_eating.user.dto.UserSignUpReqDto;
import com.ch_eatimg.ch_eating.util.vaild.ValidationUtils;
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
        name = "USER_ID",
        sequenceName = "USER_ID_SEQ",
        initialValue = 1,
        allocationSize = 1
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

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserRole> userRoles;

    public static User toEntity(UserSignUpReqDto dto, List<Role> roles) {
        // 아이디와 비밀번호 형식 검증
        if (!ValidationUtils.isUserIdValid(dto.getUserId())) {
            throw new IllegalArgumentException("아이디는 6~12자의 영문자, 숫자, 하이픈, 언더스코어만 사용할 수 있습니다.");
        }

        if (!ValidationUtils.isPasswordValid(dto.getUserPassword())) {
            throw new IllegalArgumentException("비밀번호는 8~15자 사이이며, 최소 하나의 소문자, 숫자, 특수문자(!@#$%)를 포함해야 합니다.");
        }

        User user = User.builder()
                .userId(dto.getUserId())
                .userPassword(dto.getUserPassword())
                .userName(dto.getUserName())
                .build();

        List<UserRole> userRoles = roles.stream()
                .map(role -> new UserRole(null, user, role))
                .collect(Collectors.toList());

        user.userRoles = userRoles;

        return user;
    }
}
