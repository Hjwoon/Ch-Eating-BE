package com.ch_eatimg.ch_eating.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestResDto {
    private Long testId;
    private String testName;
    private String userId;
    private String testResult;
    private String testWin;
    private LocalDateTime createAt;
}
