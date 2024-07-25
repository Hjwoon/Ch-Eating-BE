package com.ch_eatimg.ch_eating.user.service;

import com.ch_eatimg.ch_eating.user.dto.UserSignInReqDto;
import com.ch_eatimg.ch_eating.user.dto.UserSignInResDto;
import com.ch_eatimg.ch_eating.user.dto.UserSignUpReqDto;

public interface UserService {
    String signUp(UserSignUpReqDto dto);
    UserSignInResDto signIn(UserSignInReqDto dto);
}