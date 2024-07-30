package com.ch_eatimg.ch_eating.meal.dto;

import lombok.*;

public class MealListDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealResponse {

        private Long mealId; // 식사량 ID
        private String mealType; // 식사 종류
        private String mealBrand; // 식사 브랜드명
        private String mealName; // 식사 메뉴명
        private String mealAmount; // 식사량
        private String mealDetail; // 세부사항

    }
}