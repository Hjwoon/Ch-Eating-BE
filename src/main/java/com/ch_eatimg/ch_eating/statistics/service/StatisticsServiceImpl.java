package com.ch_eatimg.ch_eating.statistics.service;

import com.ch_eatimg.ch_eating.domain.Test;
import com.ch_eatimg.ch_eating.statistics.dto.StatisticsMonthlyResponseDto;
import com.ch_eatimg.ch_eating.statistics.dto.StatisticsResponseDto;
import com.ch_eatimg.ch_eating.test.repository.TestRepository;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final TestRepository testRepository;

    public StatisticsServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    // 주간 조회(요일별로)
    @Override @Transactional(readOnly = true)
    public ResponseEntity<CustomApiResponse<StatisticsResponseDto>> getFakeHungerStatistics(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE).plusDays(1);

            List<Test> tests = testRepository.findByTestResultAndCreateAtBetween("가짜 배고픔", start.atStartOfDay(), end.atStartOfDay());

            Map<LocalDate, List<Test>> groupedTestsByDate = tests.stream()
                    .collect(Collectors.groupingBy(test -> test.getCreateAt().toLocalDate()));

            List<StatisticsResponseDto.PeriodStatistics> dailyStatistics = groupedTestsByDate.entrySet().stream()
                    .map(entry -> {
                        LocalDate date = entry.getKey();
                        List<Test> testsForDate = entry.getValue();

                        int totalOccurrences = testsForDate.size();
                        int totalFailures = (int) testsForDate.stream()
                                .filter(test -> "패배".equals(test.getTestWin()))
                                .count();

                        return StatisticsResponseDto.PeriodStatistics.builder()
                                .date(date.toString())
                                .totalFakeHungerOccurrences(totalOccurrences)
                                .totalFakeHungerFailures(totalFailures)
                                .build();
                    })
                    .collect(Collectors.toList());

            int[] timeDistribution = new int[24];
            tests.forEach(test -> {
                int hour = test.getCreateAt().getHour();
                timeDistribution[hour]++;
            });

            StatisticsResponseDto responseDto = StatisticsResponseDto.builder()
                    .periodStatistics(dailyStatistics)
                    .totalFakeHungerTimeDistribution(Arrays.stream(timeDistribution).boxed().collect(Collectors.toList()))
                    .build();

            return ResponseEntity.ok(CustomApiResponse.createSuccess(200, responseDto, "가짜 배고픔 통계 조회에 성공했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    CustomApiResponse.createFailWithout(500, "예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
            );
        }
    }

    // 월간 조회(주간별로)
    // 월간 조회(주간별로)
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<CustomApiResponse<StatisticsMonthlyResponseDto>> getFakeHungerStatisticsMonth(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

            // 주간 시작일과 종료일을 계산
            LocalDate currentStart = start.with(DayOfWeek.MONDAY);
            LocalDate currentEnd = currentStart.plusDays(6); // 7일간의 주차 계산

            List<StatisticsMonthlyResponseDto.MonthlyStatistics> weeklyStatistics = new ArrayList<>();
            int[] timeDistribution = new int[24];

            while (!currentStart.isAfter(end)) {
                LocalDateTime periodStart = currentStart.atStartOfDay();
                LocalDateTime periodEnd = currentEnd.plusDays(1).atStartOfDay();

                List<Test> tests = testRepository.findByTestResultAndCreateAtBetween("가짜 배고픔", periodStart, periodEnd);

                int totalOccurrences = tests.size();
                int totalFailures = (int) tests.stream()
                        .filter(test -> "패배".equals(test.getTestWin()))
                        .count();

                weeklyStatistics.add(StatisticsMonthlyResponseDto.MonthlyStatistics.builder()
                        .date(currentStart.toString() + " - " + currentEnd.toString())
                        .totalFakeHungerOccurrences(totalOccurrences)
                        .totalFakeHungerFailures(totalFailures)
                        .build());

                tests.forEach(test -> {
                    int hour = test.getCreateAt().getHour();
                    timeDistribution[hour]++;
                });

                currentStart = currentStart.plusWeeks(1); // 다음 주차 시작
                currentEnd = currentStart.plusDays(6); // 다음 주차 종료
            }

            StatisticsMonthlyResponseDto responseDto = StatisticsMonthlyResponseDto.builder()
                    .periodStatistics(weeklyStatistics)
                    .totalFakeHungerTimeDistribution(Arrays.stream(timeDistribution).boxed().collect(Collectors.toList()))
                    .build();

            return ResponseEntity.ok(CustomApiResponse.createSuccess(200, responseDto, "가짜 배고픔 통계 조회에 성공했습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    CustomApiResponse.createFailWithout(500, "예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
            );
        }
    }
}
