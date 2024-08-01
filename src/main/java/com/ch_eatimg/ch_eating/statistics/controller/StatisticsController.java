package com.ch_eatimg.ch_eating.statistics.controller;

import com.ch_eatimg.ch_eating.statistics.dto.StatisticsMonthlyResponseDto;
import com.ch_eatimg.ch_eating.statistics.dto.StatisticsResponseDto;
import com.ch_eatimg.ch_eating.statistics.service.StatisticsService;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    public ResponseEntity<CustomApiResponse<StatisticsResponseDto>> getFakeHungerStatistics(
            HttpServletRequest request, @Valid @RequestBody StatisticsResponseDto dto,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        CustomApiResponse<StatisticsResponseDto> result = statisticsService.getFakeHungerStatistics(request, dto, startDate, endDate);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    // 월간 조회(한 주씩 합산)
    @GetMapping("/monthly")
    public ResponseEntity<CustomApiResponse<StatisticsMonthlyResponseDto>> getFakeHungerStatisticsMonth(
            HttpServletRequest request, @Valid @RequestBody StatisticsMonthlyResponseDto dto,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        CustomApiResponse<StatisticsMonthlyResponseDto> result = statisticsService.getFakeHungerStatisticsMonth(request, dto, startDate, endDate);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

}
