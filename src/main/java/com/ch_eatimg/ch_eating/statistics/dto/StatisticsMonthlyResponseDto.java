package com.ch_eatimg.ch_eating.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsMonthlyResponseDto {
    private List<MonthlyStatistics> periodStatistics;
    private List<Integer> totalFakeHungerTimeDistribution;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyStatistics {
        private String date;
        private int totalFakeHungerOccurrences;
        private int totalFakeHungerFailures;
    }
}
