package com.ch_eatimg.ch_eating.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSignInResDto {
    private String accessToken;
    private String refreshToken;
}
