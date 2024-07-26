package com.ch_eatimg.ch_eating.user.controller;

import com.ch_eatimg.ch_eating.user.dto.UserSignInReqDto;
import com.ch_eatimg.ch_eating.user.dto.UserSignInResDto;
import com.ch_eatimg.ch_eating.user.dto.UserSignUpReqDto;
import com.ch_eatimg.ch_eating.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserSignUpReqDto dto) {
        String result = userService.signUp(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody @Valid UserSignInReqDto dto, HttpServletResponse response) {
        UserSignInResDto result = userService.signIn(dto, response);
        return ResponseEntity.ok(result);
    }
}