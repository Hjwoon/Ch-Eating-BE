package com.ch_eatimg.ch_eating.test.service;

import com.ch_eatimg.ch_eating.test.dto.TestReqDto;
import com.ch_eatimg.ch_eating.test.dto.TestResDto;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface TestService {
    CustomApiResponse<TestResDto> createTest(HttpServletRequest request, TestReqDto dto);
    CustomApiResponse<List<TestResDto>> getTestsByUser(HttpServletRequest request);
    CustomApiResponse<TestResDto> updateTestWin(Long testId, String testWin);
}
