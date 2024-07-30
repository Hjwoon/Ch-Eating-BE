package com.ch_eatimg.ch_eating.meal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealCreateResponseDto {
    @NotNull(message = "식사량 ID는 비워져 있을 수 없습니다.")
    private Long mealId;

    private LocalDateTime createAt;
}
