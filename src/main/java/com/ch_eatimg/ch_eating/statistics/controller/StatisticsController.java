package com.ch_eatimg.ch_eating.statistics.controller;

import com.ch_eatimg.ch_eating.statistics.dto.StatisticsResponseDto;
import com.ch_eatimg.ch_eating.statistics.service.StatisticsService;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    // 주간 조회(요일별)
    @GetMapping("/week")
    public ResponseEntity<CustomApiResponse<StatisticsResponseDto>> getFakeHungerStatistics(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return statisticsService.getFakeHungerStatistics(startDate, endDate);
    }

    // 월간 조회(한 주씩 합산)
}
