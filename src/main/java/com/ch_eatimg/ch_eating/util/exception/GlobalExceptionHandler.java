package com.ch_eatimg.ch_eating.util.exception;

import com.ch_eatimg.ch_eating.exception.TokenExpiredException;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomApiResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CustomApiResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    // 추가적인 예외 핸들러들
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomApiResponse<?>> handleGeneralException(Exception e) {
        String errorMessage = "서버에서 처리 중 오류가 발생했습니다: " + e.getMessage();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<CustomApiResponse<?>> handleTokenExpiredException(TokenExpiredException e) {
        String errorMessage = "토큰이 만료되었습니다. 다시 로그인 해주세요.";
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(CustomApiResponse.createFailWithout(HttpStatus.UNAUTHORIZED.value(), errorMessage));
    }
}
