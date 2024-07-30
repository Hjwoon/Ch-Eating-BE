package com.ch_eatimg.ch_eating.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestStatisticsDto {
    private int totalWins;
    private String mostCommonDayForFakeHunger;
    private String mostCommonHourForFakeHunger;  // 가장 많이 나타난 시간대 추가
}

