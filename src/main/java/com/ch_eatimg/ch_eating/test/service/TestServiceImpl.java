package com.ch_eatimg.ch_eating.test.service;

import com.ch_eatimg.ch_eating.domain.Test;
import com.ch_eatimg.ch_eating.test.dto.*;
import com.ch_eatimg.ch_eating.test.repository.TestRepository;
import com.ch_eatimg.ch_eating.domain.User;
import com.ch_eatimg.ch_eating.security.JwtTokenProvider;
import com.ch_eatimg.ch_eating.user.repository.UserRepository;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public CustomApiResponse<TestResDto> createTest(HttpServletRequest request, TestReqDto dto) {
        try {
            String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            Test test = Test.builder()
                    .userId(user)
                    .testName(dto.getTestName())
                    .testResult(dto.getTestResult())
                    .testWin(null)
                    .build();

            test.validateFields();

            testRepository.save(test);

            return CustomApiResponse.createSuccess(
                    HttpStatus.CREATED.value(),
                    null,
                    "테스트 결과 등록 성공"
            );
        } catch (IllegalArgumentException e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.BAD_REQUEST.value(),
                    "유효하지 않은 데이터입니다: " + e.getMessage()
            );
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }

    @Override
    public CustomApiResponse<List<TestResDto>> getTestsByUser(HttpServletRequest request) {
        try {
            String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            List<TestResDto> tests = testRepository.findByUserId(user).stream()
                    .map(test -> TestResDto.builder()
                            .userId(userId)
                            .testId(test.getTestId())
                            .testName(test.getTestName())
                            .testResult(test.getTestResult())
                            .testWin(test.getTestWin())
                            .build())
                    .collect(Collectors.toList());

            return CustomApiResponse.createSuccess(
                    HttpStatus.OK.value(),
                    tests,
                    "테스트 결과 조회 성공"
            );
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }

    @Override
    public CustomApiResponse<TestResDto> updateTestWin(Long testId, String testWin) {
        try {
            Test test = testRepository.findById(testId)
                    .orElseThrow(() -> new RuntimeException("테스트를 찾을 수 없습니다."));

            if (!"가짜 배고픔".equals(test.getTestResult())) {
                return CustomApiResponse.createFailWithout(
                        HttpStatus.BAD_REQUEST.value(),
                        "해당 테스트의 태스트 명은 '가짜 배고픔'가 아닙니다. 수정 불가."
                );
            }

            if (!"승리".equals(testWin) && !"패배".equals(testWin)) {
                return CustomApiResponse.createFailWithout(
                        HttpStatus.BAD_REQUEST.value(),
                        "유효하지 않은 승패 값입니다. (승리, 패배만 가능)"
                );
            }

            test.setTestWin(testWin);
            testRepository.save(test);

            TestResDto responseDto = TestResDto.builder()
                    .testId(test.getTestId())
                    .userId(test.getUserId().getUserId())
                    .testName(test.getTestName())
                    .testResult(test.getTestResult())
                    .testWin(test.getTestWin())
                    .build();

            return CustomApiResponse.createSuccess(
                    HttpStatus.OK.value(),
                    responseDto,
                    "가짜 배고픔 테스트 승패 등록 성공"
            );
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }

    @Override
    public CustomApiResponse<List<TestResDto>> getTestsByDate(HttpServletRequest request, LocalDate date) {
        try {
            String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            DateTimeFormatter timeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");// 시간 포맷
            DateTimeFormatter timeFormatter2 = DateTimeFormatter.ofPattern("HH:mm");// 시간 포맷



            List<TestResDto> tests = testRepository.findByUserIdAndCreateAtBetween(user, startOfDay, endOfDay).stream()
                    .map(test -> TestResDto.builder()
                            .userId(userId)
                            .testId(test.getTestId())
                            .testName(test.getTestName())
                            .testResult(test.getTestResult())
                            .testWin(test.getTestWin())
                            .createDate(test.getCreateAt() != null ? test.getCreateAt().format(timeFormatter1) : null)
                            .createTime(test.getCreateAt() != null ? test.getCreateAt().format(timeFormatter2) : null)
                            .build())
                    .collect(Collectors.toList());

            return CustomApiResponse.createSuccess(
                    HttpStatus.OK.value(),
                    tests,
                    "특정 날짜의 테스트 결과 조회 성공"
            );
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }

    @Override
    public CustomApiResponse<List<TestResDto>> getTestsByMonth(HttpServletRequest request, int year, int month) {
        try {
            String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            // 월의 시작과 끝 계산
            LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.toLocalDate().lengthOfMonth()).with(LocalTime.MAX);
            DateTimeFormatter timeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 시간 포맷
            DateTimeFormatter timeFormatter2 = DateTimeFormatter.ofPattern("HH:mm"); // 시간 포맷

            List<TestResDto> tests = testRepository.findByUserIdAndCreateAtBetween(user, startOfMonth, endOfMonth).stream()
                    .map(test -> TestResDto.builder()
                            .userId(userId)
                            .testId(test.getTestId())
                            .testName(test.getTestName())
                            .testResult(test.getTestResult())
                            .testWin(test.getTestWin())
                            .createDate(test.getCreateAt() != null ? test.getCreateAt().format(timeFormatter1) : null)
                            .createTime(test.getCreateAt() != null ? test.getCreateAt().format(timeFormatter2) : null)
                            .build())
                    .collect(Collectors.toList());

            return CustomApiResponse.createSuccess(
                    HttpStatus.OK.value(),
                    tests,
                    "특정 월의 테스트 결과 조회 성공"
            );
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }

    @Override
    public CustomApiResponse<TestStatisticsDto> getFakeHungerStatistics(HttpServletRequest request, int year, int month) {
        try {
            String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.toLocalDate().lengthOfMonth()).with(LocalTime.MAX);

            List<Test> tests = testRepository.findByUserIdAndCreateAtBetween(user, startOfMonth, endOfMonth);

            long totalWins = tests.stream()
                    .filter(test -> "승리".equals(test.getTestWin()))
                    .count();

            Map<String, Long> fakeHungerCountByDayOfWeek = tests.stream()
                    .filter(test -> "가짜 배고픔".equals(test.getTestResult()))
                    .collect(Collectors.groupingBy(
                            test -> test.getCreateAt().getDayOfWeek().toString(),
                            Collectors.counting()
                    ));

            String mostCommonDayForFakeHunger = fakeHungerCountByDayOfWeek.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            // 가장 많이 나타난 시간대 찾기
            String mostCommonHourForFakeHunger = tests.stream()
                    .filter(test -> "가짜 배고픔".equals(test.getTestResult()))
                    .collect(Collectors.groupingBy(
                            test -> String.format("%02d:00", test.getCreateAt().getHour()),
                            Collectors.counting()
                    )).entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            TestStatisticsDto statistics = TestStatisticsDto.builder()
                    .totalWins((int) totalWins)
                    .mostCommonDayForFakeHunger(mostCommonDayForFakeHunger)
                    .mostCommonHourForFakeHunger(mostCommonHourForFakeHunger)
                    .build();

            return CustomApiResponse.createSuccess(
                    HttpStatus.OK.value(),
                    statistics,
                    "가짜 배고픔 통계 조회 성공"
            );
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }

    @Override
    public CustomApiResponse<TestStatisticsDto> getFakeHungerStatisticsByDateRange(HttpServletRequest request, LocalDate startDate, LocalDate endDate) {
        try {
            String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

            List<Test> tests = testRepository.findByUserIdAndCreateAtBetween(user, startDateTime, endDateTime);

            long totalWins = tests.stream()
                    .filter(test -> "승리".equals(test.getTestWin()))
                    .count();

            Map<String, Long> fakeHungerCountByDayOfWeek = tests.stream()
                    .filter(test -> "가짜 배고픔".equals(test.getTestResult()))
                    .collect(Collectors.groupingBy(
                            test -> test.getCreateAt().getDayOfWeek().toString(),
                            Collectors.counting()
                    ));

            String mostCommonDayForFakeHunger = fakeHungerCountByDayOfWeek.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            // 가장 많이 나타난 시간대 찾기
            String mostCommonHourForFakeHunger = tests.stream()
                    .filter(test -> "가짜 배고픔".equals(test.getTestResult()))
                    .collect(Collectors.groupingBy(
                            test -> String.format("%02d:00", test.getCreateAt().getHour()),
                            Collectors.counting()
                    )).entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            TestStatisticsDto statistics = TestStatisticsDto.builder()
                    .totalWins((int) totalWins)
                    .mostCommonDayForFakeHunger(mostCommonDayForFakeHunger)
                    .mostCommonHourForFakeHunger(mostCommonHourForFakeHunger)
                    .build();

            return CustomApiResponse.createSuccess(
                    HttpStatus.OK.value(),
                    statistics,
                    "가짜 배고픔 통계 조회 성공"
            );
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }

    @Override
    public CustomApiResponse<FakeHungerStatisticsDto> getFakeHungerStatisticsByDate(HttpServletRequest request, LocalDate startDate, LocalDate endDate) {
        try {
            String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

            List<Test> tests = testRepository.findByUserIdAndCreateAtBetween(user, startDateTime, endDateTime);

            List<FakeHungerStatisticsDto.PeriodStatistic> periodStatistics = new ArrayList<>();
            List<Integer> totalFakeHungerTimeDistribution = new ArrayList<>(24);

            for (int i = 0; i < 24; i++) {
                totalFakeHungerTimeDistribution.add(0);
            }

            LocalDate currentStartDate = startDate;
            while (!currentStartDate.isAfter(endDate)) {
                LocalDate currentEndDate = currentStartDate.plusDays(6);
                if (currentEndDate.isAfter(endDate)) {
                    currentEndDate = endDate;
                }

                LocalDateTime currentStartDateTime = currentStartDate.atStartOfDay();
                LocalDateTime currentEndDateTime = currentEndDate.atTime(LocalTime.MAX);

                List<Test> weekTests = tests.stream()
                        .filter(test -> test.getCreateAt().isAfter(currentStartDateTime) && test.getCreateAt().isBefore(currentEndDateTime))
                        .collect(Collectors.toList());

                int totalOccurrences = (int) weekTests.stream()
                        .filter(test -> "가짜 배고픔".equals(test.getTestResult()))
                        .count();

                int totalFailures = (int) weekTests.stream()
                        .filter(test -> "패배".equals(test.getTestWin()))
                        .count();

                periodStatistics.add(FakeHungerStatisticsDto.PeriodStatistic.builder()
                        .date(currentStartDate + " - " + currentEndDate)
                        .totalFakeHungerOccurrences(totalOccurrences)
                        .totalFakeHungerFailures(totalFailures)
                        .build());

                weekTests.stream()
                        .filter(test -> "가짜 배고픔".equals(test.getTestResult()))
                        .forEach(test -> {
                            int hour = test.getCreateAt().getHour();
                            totalFakeHungerTimeDistribution.set(hour, totalFakeHungerTimeDistribution.get(hour) + 1);
                        });

                currentStartDate = currentStartDate.plusWeeks(1);
            }

            FakeHungerStatisticsDto statistics = FakeHungerStatisticsDto.builder()
                    .periodStatistics(periodStatistics)
                    .totalFakeHungerTimeDistribution(totalFakeHungerTimeDistribution)
                    .build();

            return CustomApiResponse.createSuccess(
                    HttpStatus.OK.value(),
                    statistics,
                    "가짜 배고픔 통계 조회 성공"
            );
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }

    @Override
    public CustomApiResponse<DailyHungerStatisticsDto> getDailyHungerStatisticsByDate(HttpServletRequest request, LocalDate startDate, LocalDate endDate) {
        try {
            String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

            List<Test> tests = testRepository.findByUserIdAndCreateAtBetween(user, startDateTime, endDateTime);

            List<DailyHungerStatisticsDto.DayStatistic> dayStatistics = new ArrayList<>();
            List<Integer> totalFakeHungerTimeDistribution = new ArrayList<>(24);

            for (int i = 0; i < 24; i++) {
                totalFakeHungerTimeDistribution.add(0);
            }

            Map<LocalDate, List<Test>> testsGroupedByDay = tests.stream()
                    .collect(Collectors.groupingBy(test -> test.getCreateAt().toLocalDate()));

            for (LocalDate date : startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList())) {
                List<Test> dayTests = testsGroupedByDay.getOrDefault(date, new ArrayList<>());

                int totalOccurrences = (int) dayTests.stream()
                        .filter(test -> "가짜 배고픔".equals(test.getTestResult()))
                        .count();

                int totalFailures = (int) dayTests.stream()
                        .filter(test -> "패배".equals(test.getTestWin()))
                        .count();

                String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

                dayStatistics.add(DailyHungerStatisticsDto.DayStatistic.builder()
                        .date(date.toString())
                        .dayOfWeek(dayOfWeek)
                        .totalFakeHungerOccurrences(totalOccurrences)
                        .totalFakeHungerFailures(totalFailures)
                        .build());

                dayTests.stream()
                        .filter(test -> "가짜 배고픔".equals(test.getTestResult()))
                        .forEach(test -> {
                            int hour = test.getCreateAt().getHour();
                            totalFakeHungerTimeDistribution.set(hour, totalFakeHungerTimeDistribution.get(hour) + 1);
                        });
            }

            DailyHungerStatisticsDto statistics = DailyHungerStatisticsDto.builder()
                    .periodStatistics(dayStatistics)
                    .totalFakeHungerTimeDistribution(totalFakeHungerTimeDistribution)
                    .build();

            return CustomApiResponse.createSuccess(
                    HttpStatus.OK.value(),
                    statistics,
                    "가짜 배고픔 통계 조회에 성공했습니다."
            );
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }
}
