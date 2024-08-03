package com.ch_eatimg.ch_eating.test.service;

import com.ch_eatimg.ch_eating.test.dto.*;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.List;

public interface TestService {
    CustomApiResponse<TestResDto> createTest(HttpServletRequest request, TestReqDto dto);
    CustomApiResponse<List<TestResDto>> getTestsByUser(HttpServletRequest request);
    CustomApiResponse<TestResDto> updateTestWin(Long testId, String testWin);
    CustomApiResponse<List<TestResDto>> getTestsByDate(HttpServletRequest request, LocalDate date);
    CustomApiResponse<List<TestResDto>> getTestsByMonth(HttpServletRequest request, int year, int month);
    CustomApiResponse<TestStatisticsDto> getFakeHungerStatistics(HttpServletRequest request, int year, int month);
    CustomApiResponse<TestStatisticsDto> getFakeHungerStatisticsByDateRange(HttpServletRequest request, LocalDate startDate, LocalDate endDate);
    CustomApiResponse<FakeHungerStatisticsDto> getFakeHungerStatisticsByDate(HttpServletRequest request, LocalDate startDate, LocalDate endDate);
    CustomApiResponse<DailyHungerStatisticsDto> getDailyHungerStatisticsByDate(HttpServletRequest request, LocalDate startDate, LocalDate endDate);
}