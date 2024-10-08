package com.ch_eatimg.ch_eating.test.controller;

import com.ch_eatimg.ch_eating.test.dto.*;
import com.ch_eatimg.ch_eating.test.service.TestService;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping("/test")
    public ResponseEntity<CustomApiResponse<TestResDto>> createTest(
            HttpServletRequest request, @Valid @RequestBody TestReqDto dto) {
        CustomApiResponse<TestResDto> result = testService.createTest(request, dto);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @GetMapping("/getTest")
    public ResponseEntity<CustomApiResponse<List<TestResDto>>> getTestsByUser(HttpServletRequest request) {
        CustomApiResponse<List<TestResDto>> result = testService.getTestsByUser(request);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @PatchMapping("/{testId}/win")
    public ResponseEntity<CustomApiResponse<TestResDto>> updateTestWin(
            @PathVariable Long testId, @RequestBody TestUpdateReqDto dto) {
        CustomApiResponse<TestResDto> result = testService.updateTestWin(testId, dto.getTestWin());
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @GetMapping("/byDate")
    public ResponseEntity<CustomApiResponse<List<TestResDto>>> getTestsByDate(
            HttpServletRequest request, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        CustomApiResponse<List<TestResDto>> result = testService.getTestsByDate(request, date);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @GetMapping("/byMonth")
    public ResponseEntity<CustomApiResponse<List<TestResDto>>> getTestsByMonth(
            HttpServletRequest request,
            @RequestParam int year,
            @RequestParam int month) {
        CustomApiResponse<List<TestResDto>> result = testService.getTestsByMonth(request, year, month);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @GetMapping("/statistics")
    public ResponseEntity<CustomApiResponse<TestStatisticsDto>> getFakeHungerStatistics(
            HttpServletRequest request,
            @RequestParam int year,
            @RequestParam int month) {
        CustomApiResponse<TestStatisticsDto> result = testService.getFakeHungerStatistics(request, year, month);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @GetMapping("/statistics/byDateRange")
    public ResponseEntity<CustomApiResponse<TestStatisticsDto>> getFakeHungerStatisticsByDateRange(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        CustomApiResponse<TestStatisticsDto> result = testService.getFakeHungerStatisticsByDateRange(request, startDate, endDate);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @GetMapping("/statistics/weekly")
    public ResponseEntity<CustomApiResponse<DailyHungerStatisticsDto>> getDailyHungerStatisticsByDate(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        CustomApiResponse<DailyHungerStatisticsDto> result = testService.getDailyHungerStatisticsByDate(request, startDate, endDate);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @GetMapping("/statistics/monthly")
    public ResponseEntity<CustomApiResponse<FakeHungerStatisticsDto>> getFakeHungerStatisticsByDate(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        CustomApiResponse<FakeHungerStatisticsDto> result = testService.getFakeHungerStatisticsByDate(request, startDate, endDate);
        return ResponseEntity.status(result.getStatus()).body(result);
    }
}
