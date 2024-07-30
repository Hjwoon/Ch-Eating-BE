package com.ch_eatimg.ch_eating.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class StatisticsResponseDto {

    private List<DailyStatistics> periodStatistics;
    private List<Integer> totalFakeHungerTimeDistribution;

    @Data
    @Builder
    @AllArgsConstructor
    public static class DailyStatistics {
        private String date;
        private int totalFakeHungerOccurrences;
        private int totalFakeHungerFailures;
    }
}