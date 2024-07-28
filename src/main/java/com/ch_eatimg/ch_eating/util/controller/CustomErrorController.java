package com.ch_eatimg.ch_eating.util.controller;

import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<CustomApiResponse<?>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            switch (statusCode) {
                case 400 -> {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new CustomApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, "잘못된 요청입니다. 요청을 확인하고 다시 시도해주세요."));
                }
                case 403 -> {
                    return ResponseEntity
                            .status(HttpStatus.FORBIDDEN)
                            .body(new CustomApiResponse<>(HttpStatus.FORBIDDEN.value(), null, "접근이 금지되었습니다. 권한을 확인해주세요."));
                }
                case 404 -> {
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(new CustomApiResponse<>(HttpStatus.NOT_FOUND.value(), null, "요청한 경로를 찾을 수 없습니다. URL을 확인해주세요."));
                }
                case 405 -> {
                    return ResponseEntity
                            .status(HttpStatus.METHOD_NOT_ALLOWED)
                            .body(new CustomApiResponse<>(HttpStatus.METHOD_NOT_ALLOWED.value(), null, "허용되지 않은 메소드입니다. 요청 방식을 확인해주세요."));
                }
                case 500 -> {
                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new CustomApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
                }
                default -> {
                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new CustomApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "알 수 없는 오류가 발생했습니다. 관리자에게 문의하세요."));
                }
            }
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "알 수 없는 오류가 발생했습니다. 관리자에게 문의하세요."));
    }
}
