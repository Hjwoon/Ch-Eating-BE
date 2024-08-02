package com.ch_eatimg.ch_eating.statistics.controller;

import com.ch_eatimg.ch_eating.statistics.dto.StatisticsMonthlyResponseDto;
import com.ch_eatimg.ch_eating.statistics.dto.StatisticsResponseDto;
import com.ch_eatimg.ch_eating.statistics.service.StatisticsService;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    // 주간 조회(요일별)
    @GetMapping("/weekly")
    private CustomApiResponse<StatisticsResponseDto> getFakeHungerStatistics(
            @RequestParam String userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return statisticsService.getFakeHungerStatistics(userId, startDate, endDate);
    }

    // 월간 조회(한 주씩 합산)
    @GetMapping("/monthly")
    private CustomApiResponse<StatisticsMonthlyResponseDto> getFakeHungerStatisticsMonth(
            @RequestParam String userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return statisticsService.getFakeHungerStatisticsMonth(userId, startDate, endDate);
    }
}
