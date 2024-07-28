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
            // 서버 내부 오류에 대한 응답 생성
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenExpiredResponse("액세스 토큰이 만료되었습니다. 리프레쉬 토큰을 사용해 새 액세스 토큰을 발급받으세요."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenExpiredResponse("로그인 상태가 아닙니다. 다시 로그인 해주세요."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
        try {
            userService.deleteUser(request);
            return ResponseEntity.ok().body("회원 탈퇴가 완료되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("회원 탈퇴 실패: 유효한 토큰이 없거나 만료되었습니다. 로그인 후 다시 시도해주세요.");
        }
    }
}
