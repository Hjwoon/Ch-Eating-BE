package com.ch_eatimg.ch_eating.meal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MealDeleteRequestDto {

    @NotNull(message = "식사량 ID는 비워져 있을 수 없습니다.")
    private String mealId; // 삭제할 식사량의 ID

    @NotNull(message = "유저 ID는 비워져 있을 수 없습니다.")
    private String userId; // 유저 ID
}