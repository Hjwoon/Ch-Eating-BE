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
        name = "AFTER_ID",
        sequenceName = "AFTER_ID_SEQ",
        initialValue = 1,
        allocationSize = 1
)

@Table(name = "AFTERTESTS")
public class AfterTest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AFTER_ID_SEQ_GENERATOR")
    @Column(name = "after_id")
    private Long afterId;

    //한명의 유저는 여러개의 식사 후검사를 작성할 수 있다.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    //테스트 결과
    @Column(name = "after_result")
    private String afterResult;

    //테스트 승패
    @Column(name = "after_win")
    private String afterWin;
}

