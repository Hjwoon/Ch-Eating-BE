package com.ch_eatimg.ch_eating.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserMyPageResDto {
    private String userId;       // 사용자 ID
    private String userName;     // 사용자 이름
}
