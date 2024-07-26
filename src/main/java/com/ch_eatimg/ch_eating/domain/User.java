package com.ch_eatimg.ch_eating.domain;

import com.ch_eatimg.ch_eating.user.dto.UserSignUpReqDto;
import com.ch_eatimg.ch_eating.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
            generator = "USER_ID"
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
    private List<UserRole> userRoles = new ArrayList<>(); // 빈 리스트로 초기화

    public static User toEntity(UserSignUpReqDto dto, Role defaultRole) {
        User user = User.builder()
                .userId(dto.getUserId())
                .userPassword(dto.getUserPassword())
                .userName(dto.getUserName())
                .userPhone(dto.getUserPhone())
                .userRoles(new ArrayList<>()) // 여기서도 빈 리스트로 초기화
                .build();

        UserRole userRole = new UserRole(user, defaultRole);
        user.getUserRoles().add(userRole); // 역할을 추가

        return user;
    }
}
