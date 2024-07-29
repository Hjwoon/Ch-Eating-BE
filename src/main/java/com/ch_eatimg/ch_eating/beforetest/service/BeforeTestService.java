package com.ch_eatimg.ch_eating.beforetest.service;

import com.ch_eatimg.ch_eating.beforetest.dto.BeforeTestReqDto;
import com.ch_eatimg.ch_eating.beforetest.dto.BeforeTestResDto;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface BeforeTestService {
    CustomApiResponse<BeforeTestResDto> createBeforeTest(HttpServletRequest request, BeforeTestReqDto dto);
    CustomApiResponse<List<BeforeTestResDto>> getBeforeTestsByUser(HttpServletRequest request);
}
