package com.ch_eatimg.ch_eating.user.service;

import com.ch_eatimg.ch_eating.user.dto.*;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    @Transactional
    CustomApiResponse<UserSignUpResDto> signUp(UserSignUpReqDto dto, HttpServletResponse response);

    CustomApiResponse<UserSignInResDto> signIn(UserSignInReqDto dto, HttpServletResponse response);

    @Transactional(readOnly = true)
    UserInfoDto getUserInfo(HttpServletRequest request);

    @Transactional
    void deleteUser(HttpServletRequest request);

    void logout(HttpServletRequest request, HttpServletResponse response);
}
