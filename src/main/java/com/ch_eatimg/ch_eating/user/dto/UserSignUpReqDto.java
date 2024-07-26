package com.ch_eatimg.ch_eating.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

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

    @NotNull
    List<String> userRoles;
}
