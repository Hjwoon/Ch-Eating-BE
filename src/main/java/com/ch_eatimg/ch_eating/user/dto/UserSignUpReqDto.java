package com.ch_eatimg.ch_eating.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class UserSignUpReqDto {
    @NotNull
    private String userId;

    @NotNull
    private String userPassword;

    @NotNull
    private String userName;

    @NotNull
    private String userPhone;
}
