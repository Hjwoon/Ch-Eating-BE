package com.ch_eatimg.ch_eating.user.service;

import com.ch_eatimg.ch_eating.user.dto.UserSignInReqDto;
import com.ch_eatimg.ch_eating.user.dto.UserSignInResDto;
import com.ch_eatimg.ch_eating.user.dto.UserSignUpReqDto;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
    String signUp(UserSignUpReqDto dto);
    UserSignInResDto signIn(UserSignInReqDto dto, HttpServletResponse response);
}