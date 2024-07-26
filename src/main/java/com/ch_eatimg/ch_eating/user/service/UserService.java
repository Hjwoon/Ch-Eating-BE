package com.ch_eatimg.ch_eating.user.service;

import com.ch_eatimg.ch_eating.user.dto.UserInfoDto;
import com.ch_eatimg.ch_eating.user.dto.UserSignInReqDto;
import com.ch_eatimg.ch_eating.user.dto.UserSignInResDto;
import com.ch_eatimg.ch_eating.user.dto.UserSignUpReqDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    String signUp(UserSignUpReqDto dto);
    UserSignInResDto signIn(UserSignInReqDto dto, HttpServletResponse response);

    @Transactional(readOnly = true)
    UserInfoDto getUserInfo(HttpServletRequest request);
}
