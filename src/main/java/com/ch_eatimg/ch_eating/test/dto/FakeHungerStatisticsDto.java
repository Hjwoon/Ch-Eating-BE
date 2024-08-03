package com.ch_eatimg.ch_eating.test.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FakeHungerStatisticsDto {

    @Builder
    @Getter
    @Setter
    public static class PeriodStatistic {
        private String date;
        private int totalFakeHungerOccurrences;
        private int totalFakeHungerFailures;
    }

    private List<PeriodStatistic> periodStatistics;
    private List<Integer> totalFakeHungerTimeDistribution;
}
