package com.ch_eatimg.ch_eating.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignInReqDto {
    private String userId;
    private String userPassword;
}