package com.ch_eatimg.ch_eating.test.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DailyHungerStatisticsDto {
    private List<DayStatistic> periodStatistics;
    private List<Integer> totalFakeHungerTimeDistribution;

    @Data
    @Builder
    public static class DayStatistic {
        private String date;
        private String dayOfWeek;
        private int totalFakeHungerOccurrences;
        private int totalFakeHungerFailures;
    }
}
