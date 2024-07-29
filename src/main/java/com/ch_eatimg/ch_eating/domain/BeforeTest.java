package com.ch_eatimg.ch_eating.domain;

import com.ch_eatimg.ch_eating.util.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "BEFORE_ID",
        sequenceName = "BEFORE_ID_SEQ",
        initialValue = 1,
        allocationSize = 1
)

@Table(name = "BEFORETESTS")
public class BeforeTest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BEFORE_ID_SEQ_GENERATOR")
    @Column(name = "before_id")
    @NotNull
    private Long beforeId;

    //한명의 유저는 여러개의 식사 전검사를 작성할 수 있다.
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User userId;

    //테스트 결과
    @Column(name = "before_result")
    @NotNull
    private String beforeResult;

    //테스트 승패
    @Column(name = "before_win")
    private String beforeWin;
}
