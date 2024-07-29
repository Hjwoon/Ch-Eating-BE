package com.ch_eatimg.ch_eating.test.controller;

import com.ch_eatimg.ch_eating.test.dto.TestReqDto;
import com.ch_eatimg.ch_eating.test.dto.TestResDto;
import com.ch_eatimg.ch_eating.test.dto.TestUpdateReqDto;
import com.ch_eatimg.ch_eating.test.service.TestService;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
