package com.ch_eatimg.ch_eating.user.controller;

import com.ch_eatimg.ch_eating.exception.TokenExpiredException;
import com.ch_eatimg.ch_eating.user.dto.*;
import com.ch_eatimg.ch_eating.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpReqDto dto) {
        try {
            String result = userService.signUp(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody UserSignInReqDto dto, HttpServletResponse response) {
        try {
            UserSignInResDto result = userService.signIn(dto, response);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/isLogin")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        try {
            UserInfoDto userInfo = userService.getUserInfo(request);
            return ResponseEntity.ok(userInfo);
        } catch (TokenExpiredException e) {
            // 액세스 토큰이 만료되었으나 리프레쉬 토큰이 유효한 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenExpiredResponse("액세스 토큰이 만료되었습니다. 리프레쉬 토큰을 사용해 새 액세스 토큰을 발급받으세요."));
        } catch (RuntimeException e) {
            // 모든 토큰이 만료되었거나 기타 오류
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenExpiredResponse("로그인 상태가 아닙니다. 다시 로그인 해주세요."));
        }
    }
}
