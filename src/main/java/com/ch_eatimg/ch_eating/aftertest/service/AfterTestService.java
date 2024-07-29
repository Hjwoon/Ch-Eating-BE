package com.ch_eatimg.ch_eating.aftertest.service;

import com.ch_eatimg.ch_eating.aftertest.dto.AfterTestReqDto;
import com.ch_eatimg.ch_eating.aftertest.dto.AfterTestResDto;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AfterTestService {
    CustomApiResponse<AfterTestResDto> createAfterTest(HttpServletRequest request, AfterTestReqDto dto);
    CustomApiResponse<List<AfterTestResDto>> getAfterTestsByUser(HttpServletRequest request);
}
