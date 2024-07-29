package com.ch_eatimg.ch_eating.aftertest.controller;

import com.ch_eatimg.ch_eating.aftertest.dto.AfterTestReqDto;
import com.ch_eatimg.ch_eating.aftertest.dto.AfterTestResDto;
import com.ch_eatimg.ch_eating.aftertest.service.AfterTestService;
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
public class AfterTestController {

    private final AfterTestService afterTestService;

    @PostMapping("/afterTest")
    public ResponseEntity<CustomApiResponse<AfterTestResDto>> createAfterTest(
            HttpServletRequest request,@Valid @RequestBody AfterTestReqDto dto) {
        CustomApiResponse<AfterTestResDto> result = afterTestService.createAfterTest(request, dto);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @GetMapping("/getAfterTest")
    public ResponseEntity<CustomApiResponse<List<AfterTestResDto>>> getAfterTestsByUser(HttpServletRequest request) {
        CustomApiResponse<List<AfterTestResDto>> result = afterTestService.getAfterTestsByUser(request);
        return ResponseEntity.status(result.getStatus()).body(result);
    }
}
