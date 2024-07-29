package com.ch_eatimg.ch_eating.test.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestReqDto {

    @NotEmpty(message = "테스트 명은 필수 항목입니다.")
    private String testName;

    @NotEmpty(message = "테스트 결과는 필수 항목입니다.")
    private String testResult;
}
