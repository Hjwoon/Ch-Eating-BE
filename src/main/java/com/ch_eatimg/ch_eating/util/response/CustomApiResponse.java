package com.ch_eatimg.ch_eating.util.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//모든 필드를 매개 변수로 받는 생성자를 만들어주는 어노테이션 (lombok 라이브러리)
@AllArgsConstructor
//아무 필드도 갖지 않는 생성자 (lombok 라이브러리)
@NoArgsConstructor
//Getter , Setter를 자동으로 만들어주는 어노테이션 (lombok 라이브러리)
@Getter
@Setter
public class CustomApiResponse<T> {
    //status, data, message

    private int status;

    private T data;

    private String message;


    //성공
    // static : CustomApiResponse 클래스를 new로 선언하지 않아도 사용할 수 있음
    public static <T> CustomApiResponse<T> createSuccess(int status, T data, String message){
        return new CustomApiResponse<>(status,data,message);
    }

    //실패
    public static <T> CustomApiResponse<T> createFailWithout(int status, String message){
        return new CustomApiResponse<>(status, null, message);
    }
}
