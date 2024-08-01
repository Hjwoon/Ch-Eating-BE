package com.ch_eatimg.ch_eating.statistics.service;

import com.ch_eatimg.ch_eating.statistics.dto.StatisticsMonthlyResponseDto;
import com.ch_eatimg.ch_eating.statistics.dto.StatisticsResponseDto;
import com.ch_eatimg.ch_eating.test.dto.TestReqDto;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface StatisticsService {
    CustomApiResponse<StatisticsResponseDto> getFakeHungerStatistics(HttpServletRequest request, StatisticsResponseDto dto, String startDate, String endDate);
    CustomApiResponse<StatisticsMonthlyResponseDto> getFakeHungerStatisticsMonth(HttpServletRequest request, StatisticsMonthlyResponseDto dto, String startDate, String endDate);

}