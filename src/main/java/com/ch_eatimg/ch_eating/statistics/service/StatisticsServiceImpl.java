package com.ch_eatimg.ch_eating.statistics.service;

import com.ch_eatimg.ch_eating.domain.Test;
import com.ch_eatimg.ch_eating.domain.User;
import com.ch_eatimg.ch_eating.security.JwtTokenProvider;
import com.ch_eatimg.ch_eating.statistics.dto.StatisticsMonthlyResponseDto;
import com.ch_eatimg.ch_eating.statistics.dto.StatisticsResponseDto;
import com.ch_eatimg.ch_eating.test.dto.TestReqDto;
import com.ch_eatimg.ch_eating.test.repository.TestRepository;
import com.ch_eatimg.ch_eating.user.repository.UserRepository;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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
    private final UserRepository userRepository;

    public StatisticsServiceImpl(TestRepository testRepository, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.testRepository = testRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    private final JwtTokenProvider jwtTokenProvider;

    // 주간 조회(요일별로)
    @Override
    @Transactional(readOnly = true)
    public CustomApiResponse<StatisticsResponseDto> getFakeHungerStatistics(HttpServletRequest request, StatisticsResponseDto dto, String startDate, String endDate) {
        try {
            // JWT 토큰에서 사용자 ID 추출
            String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            // 시작일과 종료일 파싱
            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE).plusDays(1);

            // 가짜 배고픔 테스트 데이터를 조회
            List<Test> tests = testRepository.findByTestResultAndCreateAtBetween("가짜 배고픔", start.atStartOfDay(), end.atStartOfDay());

            // 로그인된 사용자의 데이터만 필터링
            List<Test> userTests = tests.stream()
                    .filter(test -> test.getUserId().equals(userId))
                    .collect(Collectors.toList());

            // 날짜별로 테스트 데이터를 그룹화
            Map<LocalDate, List<Test>> groupedTestsByDate = userTests.stream()
                    .collect(Collectors.groupingBy(test -> test.getCreateAt().toLocalDate()));

            // 일별 통계 데이터 생성
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

            // 시간대별 통계 데이터 생성
            int[] timeDistribution = new int[24];
            userTests.forEach(test -> {
                int hour = test.getCreateAt().getHour();
                timeDistribution[hour]++;
            });

            // 응답 DTO 생성
            StatisticsResponseDto responseDto = StatisticsResponseDto.builder()
                    .periodStatistics(dailyStatistics)
                    .totalFakeHungerTimeDistribution(Arrays.stream(timeDistribution).boxed().collect(Collectors.toList()))
                    .build();

            return CustomApiResponse.createSuccess(200, responseDto, "가짜 배고픔 통계 조회에 성공했습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(500, "예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    // 월간 조회(주간별로)
    @Override @Transactional(readOnly = true)
    public CustomApiResponse<StatisticsMonthlyResponseDto> getFakeHungerStatisticsMonth(HttpServletRequest request, StatisticsMonthlyResponseDto dto, String startDate, String endDate) {
        try {
            // JWT 토큰에서 사용자 ID 추출
            String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            // 시작일과 종료일 파싱
            LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
            LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

            // 주간 시작일과 종료일을 계산
            LocalDate currentStart = start.with(DayOfWeek.MONDAY);
            LocalDate currentEnd = currentStart.plusDays(6);

            List<StatisticsMonthlyResponseDto.MonthlyStatistics> weeklyStatistics = new ArrayList<>();
            int[] timeDistribution = new int[24];

            // 주차별 통계 데이터 생성
            while (!currentStart.isAfter(end)) {
                LocalDateTime periodStart = currentStart.atStartOfDay();
                LocalDateTime periodEnd = currentEnd.plusDays(1).atStartOfDay();

                // 가짜 배고픔 테스트 데이터를 조회
                List<Test> tests = testRepository.findByTestResultAndCreateAtBetween("가짜 배고픔", periodStart, periodEnd);

                // 로그인된 사용자의 데이터만 필터링
                List<Test> userTests = tests.stream()
                        .filter(test -> test.getUserId().equals(userId))
                        .collect(Collectors.toList());

                int totalOccurrences = userTests.size();
                int totalFailures = (int) userTests.stream()
                        .filter(test -> "패배".equals(test.getTestWin()))
                        .count();

                // 주차별 통계 데이터 추가
                weeklyStatistics.add(StatisticsMonthlyResponseDto.MonthlyStatistics.builder()
                        .date(currentStart.toString() + " - " + currentEnd.toString())
                        .totalFakeHungerOccurrences(totalOccurrences)
                        .totalFakeHungerFailures(totalFailures)
                        .build());

                // 시간대별 통계 데이터 생성
                userTests.forEach(test -> {
                    int hour = test.getCreateAt().getHour();
                    timeDistribution[hour]++;
                });

                // 다음 주차로 이동
                currentStart = currentStart.plusWeeks(1);
                currentEnd = currentStart.plusDays(6);
            }

            // 응답 DTO 생성
            StatisticsMonthlyResponseDto responseDto = StatisticsMonthlyResponseDto.builder()
                    .periodStatistics(weeklyStatistics)
                    .totalFakeHungerTimeDistribution(Arrays.stream(timeDistribution).boxed().collect(Collectors.toList()))
                    .build();

            return CustomApiResponse.createSuccess(200, responseDto, "가짜 배고픔 통계 조회에 성공했습니다.");
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(500, "예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }


}
