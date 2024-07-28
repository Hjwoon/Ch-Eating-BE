package com.ch_eatimg.ch_eating.user.controller;

import com.ch_eatimg.ch_eating.exception.TokenExpiredException;
import com.ch_eatimg.ch_eating.user.dto.*;
import com.ch_eatimg.ch_eating.user.service.UserService;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
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
    public ResponseEntity<CustomApiResponse<UserSignUpResDto>> signUp(@RequestBody UserSignUpReqDto dto, HttpServletResponse response) {
        try {
            CustomApiResponse<UserSignUpResDto> result = userService.signUp(dto, response);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "회원가입 실패: " + e.getMessage())
            );
        } catch (RuntimeException e) {
            CustomApiResponse<UserSignUpResDto> errorResponse = CustomApiResponse.createFailWithout(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "내부 서버 오류: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<CustomApiResponse<UserSignInResDto>> signIn(@RequestBody UserSignInReqDto dto, HttpServletResponse response) {
        try {
            CustomApiResponse<UserSignInResDto> result = userService.signIn(dto, response);
            return ResponseEntity.status(result.getStatus()).body(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에서 처리 중 오류가 발생했습니다: " + e.getMessage())
            );
        }
    }

    @GetMapping("/isLogin")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        try {
            UserInfoDto userInfo = userService.getUserInfo(request);
            return ResponseEntity.ok(userInfo);
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenExpiredResponse("로그인 상태가 아닙니다. 다시 로그인 해주세요."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<CustomApiResponse<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        CustomApiResponse<String> result = userService.logout(request, response);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CustomApiResponse<String>> deleteUser(HttpServletRequest request) {
        CustomApiResponse<String> result = userService.deleteUser(request);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @GetMapping("/checkUserIdExists")
    public ResponseEntity<CustomApiResponse<String>> checkUserIdExists(@RequestParam(required = false) String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            CustomApiResponse<String> errorResponse = CustomApiResponse.createFailWithout(
                    HttpStatus.BAD_REQUEST.value(),
                    "아이디 쿼리 파라미터는 필수 항목입니다."
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        CustomApiResponse<String> result = userService.checkUserIdExists(userId);
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @GetMapping("/myPage")
    public ResponseEntity<CustomApiResponse<UserMyPageDto>> myPage(HttpServletRequest request) {
        CustomApiResponse<UserMyPageDto> result = userService.getMyPage(request);
        return ResponseEntity.status(result.getStatus()).body(result);
    }
}
