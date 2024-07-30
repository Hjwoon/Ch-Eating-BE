package com.ch_eatimg.ch_eating.meal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MealDto {

    @NotNull(message = "식사 종류를 입력하세요.")
    private String mealType; // 식사 종류

    private String mealBrand; // 식사 브랜드명

    @NotNull(message = "식사 메뉴명을 입력하세요.")
    private String mealName; // 식사 메뉴명

    @NotNull(message = "식사량을 입력하세요.")
    private String mealAmount; // 식사량

    private String mealDetail; // 세부사항
}
