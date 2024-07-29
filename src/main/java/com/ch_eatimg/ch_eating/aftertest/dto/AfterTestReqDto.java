package com.ch_eatimg.ch_eating.aftertest.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AfterTestReqDto {

    @NotEmpty(message = "테스트 결과는 필수 항목입니다.")
    private String afterResult;
}
