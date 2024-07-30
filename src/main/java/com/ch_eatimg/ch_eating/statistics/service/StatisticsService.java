package com.ch_eatimg.ch_eating.statistics.service;

import com.ch_eatimg.ch_eating.statistics.dto.StatisticsResponseDto;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface StatisticsService {
    ResponseEntity<CustomApiResponse<StatisticsResponseDto>> getFakeHungerStatistics(String startDate, String endDate);
}