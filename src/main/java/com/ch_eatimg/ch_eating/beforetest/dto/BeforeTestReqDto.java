package com.ch_eatimg.ch_eating.beforetest.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeforeTestReqDto {

    @NotEmpty(message = "테스트 결과는 필수 항목입니다.")
    private String beforeResult;
}
