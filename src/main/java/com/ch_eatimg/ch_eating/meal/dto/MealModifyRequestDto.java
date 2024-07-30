package com.ch_eatimg.ch_eating.meal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

public class MealModifyRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Req {
        @NotNull(message = "식사 아이디는 비워져 있을 수 없습니다.")
        private Long mealId; // 수정할 식사량의 아이디

        private String mealType; // 식사 종류

        private String mealBrand; // 식사 브랜드명

        private String mealName; // 식사 메뉴명

        private String mealAmount; // 식사량

        private String mealDetail; // 세부사항
    }
}