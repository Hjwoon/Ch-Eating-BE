package com.ch_eatimg.ch_eating.util.controller;

import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Chapter 3. CustomHandler
 * 웹서버에 존재하지 않는 경로로 사용자가 요청할 경우, 스프링 프레임워크의 ErrorController에 의해 기본 404 에러가 반환된다.
 * ErrorController 인터페이스를 구현한 커스텀컨트롤러를 작성하면 이러한 기본 에러를 우리가 원하는 형식으로 반환할 수 있다.
 * */
@RestController // ErrorController 또한 Controller이기 때문에 @RestController를 작성하여 스프링 빈으로 등록한다.
public class CustomErrorController implements ErrorController {

    // HttpServletRequest : 요청에 대한 정보를 전달하는데 사용되는 클래스
    // HttpServletRequest로부터 HttpStatusCode를 확인하고, 각 코드에 맞춰서 다른 응답을 반환하도록 작성한다.
    @RequestMapping("/error")
    public ResponseEntity<CustomApiResponse<?>> handleError(HttpServletRequest request) {

        // 1. HttpServletRequest로 부터 status code를 확인한다.
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            // 2. status code에 맞춰 오류 응답을 반환한다.
            switch (statusCode) {
                case 400 -> { // 1. 400 (Bad Request)
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new CustomApiResponse<>(HttpStatus.BAD_REQUEST.value(), null, "잘못된 요청입니다"));
                }
                case 403 -> { // 2. 403 (Forbidden)
                    return ResponseEntity
                            .status(HttpStatus.FORBIDDEN)
                            .body(new CustomApiResponse<>(HttpStatus.FORBIDDEN.value(), null, "접근이 금지되었습니다."));
                }
                case 404 -> { // 3. 404 (Not Found)
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(new CustomApiResponse<>(HttpStatus.NOT_FOUND.value(), null, "요청 경로를 찾을 수 없습니다."));
                }
                case 405 -> { // 4. 405 (Method Not Allowed)
                    return ResponseEntity
                            .status(HttpStatus.METHOD_NOT_ALLOWED)
                            .body(new CustomApiResponse<>(HttpStatus.METHOD_NOT_ALLOWED.value(), null, "허용되지 않은 메소드입니다."));
                }
                case 500 -> { // 5. 500 (Internal Server Error)
                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new CustomApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "서버 에러가 발생하였습니다."));
                }
            }

        }
        // 그 외의 모든 에러에 대해선 '알수 없는 에러' 라고 알린다.
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, "알수 없는 에러가 발생하였습니다."));
    }
}
