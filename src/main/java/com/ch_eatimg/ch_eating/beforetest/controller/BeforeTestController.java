package com.ch_eatimg.ch_eating.beforetest.controller;

import com.ch_eatimg.ch_eating.beforetest.dto.BeforeTestReqDto;
import com.ch_eatimg.ch_eating.beforetest.dto.BeforeTestResDto;
import com.ch_eatimg.ch_eating.beforetest.service.BeforeTestService;
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
public class BeforeTestController {

    private final BeforeTestService beforeTestService;

    @PostMapping("/beforeTest")
    public ResponseEntity<CustomApiResponse<BeforeTestResDto>> createBeforeTest(
            HttpServletRequest request,@Valid @RequestBody BeforeTestReqDto dto) {
        CustomApiResponse<BeforeTestResDto> result = beforeTestService.createBeforeTest(request, dto);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @GetMapping("/getBeforeTest")
    public ResponseEntity<CustomApiResponse<List<BeforeTestResDto>>> getBeforeTestsByUser(HttpServletRequest request) {
        CustomApiResponse<List<BeforeTestResDto>> result = beforeTestService.getBeforeTestsByUser(request);
        return ResponseEntity.status(result.getStatus()).body(result);
    }
}
