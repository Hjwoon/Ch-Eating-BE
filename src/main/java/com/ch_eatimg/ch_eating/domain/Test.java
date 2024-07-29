package com.ch_eatimg.ch_eating.domain;

import com.ch_eatimg.ch_eating.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "TEST_ID",
        sequenceName = "TEST_ID_SEQ",
        initialValue = 1,
        allocationSize = 1
)

@Table(name = "TEST")
public class Test extends BaseEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "test_id_seq"
    )
    @Column(name = "test_id")
    private Long testId;

    //한명의 유저는 여러개의 식사 테스트를 할 수 있다.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name="test_name")
    private String testName;

    //테스트 결과
    @Column(name = "test_result")
    private String testResult;

    //테스트 승패
    @Column(name = "test_win")
    private String testWin;
}

