package com.ch_eatimg.ch_eating.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDto {
    private String userId;       // 사용자 ID
    private String userName;     // 사용자 이름
    private String userPhone;   // 사용자 전화번호
    private String accessToken;  // 엑세스 토큰
}
