package com.ch_eatimg.ch_eating.domain;

import com.ch_eatimg.ch_eating.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name="test_name")
    private String testName;

    @Column(name = "test_result")
    private String testResult;

    @Column(name = "test_win")
    private String testWin;

    public void validateFields() {
        if (!isValidTestName(this.testName) || !isValidTestResult(this.testResult)) {
            throw new IllegalArgumentException("테스트 이름과 결과는 '식전 배고픔 테스트', '식후 배고픔 테스트', '진짜 배고픔', '가짜 배고픔' 중 하나여야 합니다.");
        }
    }

    private boolean isValidTestName(String testName) {
        return "식전 배고픔 테스트".equals(testName) || "식후 배고픔 테스트".equals(testName);
    }

    private boolean isValidTestResult(String testResult) {
        return "진짜 배고픔".equals(testResult) || "가짜 배고픔".equals(testResult);
    }
}
