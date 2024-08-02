package com.ch_eatimg.ch_eating.statistics.service;

import com.ch_eatimg.ch_eating.statistics.dto.StatisticsMonthlyResponseDto;
import com.ch_eatimg.ch_eating.statistics.dto.StatisticsResponseDto;
import com.ch_eatimg.ch_eating.test.dto.TestReqDto;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface StatisticsService {

    // 주간 조회(요일별로)
    @Transactional(readOnly = true)
    CustomApiResponse<StatisticsResponseDto> getFakeHungerStatistics(String userId, String startDate, String endDate);


    // 월간 조회(주간별로)
    @Transactional(readOnly = true)
    CustomApiResponse<StatisticsMonthlyResponseDto> getFakeHungerStatisticsMonth(String userId, String startDate, String endDate);
}