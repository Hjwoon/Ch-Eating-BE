package com.ch_eatimg.ch_eating.meal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MealCreateRequestDto {

    @NotNull(message = "유저 아이디는 비워져 있을 수 없습니다.")
    private String userId; // 식사량 생성자 아이디

    @NotNull(message = "식사 데이터를 입력하세요.")
    private List<MealDto> meals;
}