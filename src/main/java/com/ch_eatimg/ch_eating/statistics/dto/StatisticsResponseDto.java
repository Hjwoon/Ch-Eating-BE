package com.ch_eatimg.ch_eating.statistics.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatisticsResponseDto {

    private List<PeriodStatistics> periodStatistics;
    private List<Integer> totalFakeHungerTimeDistribution;

    @Data
    @Builder
    public static class PeriodStatistics {
        private String date;
        private int totalFakeHungerOccurrences;
        private int totalFakeHungerFailures;
    }
}
