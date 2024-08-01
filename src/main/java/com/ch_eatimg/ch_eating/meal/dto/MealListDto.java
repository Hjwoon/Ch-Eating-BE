package com.ch_eatimg.ch_eating.meal.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealListDto {

    private Long mealId; // 식사량 ID
    private String mealType; // 식사 종류
    private String mealBrand; // 식사 브랜드명
    private String mealName; // 식사 메뉴명
    private String mealAmount; // 식사량
    private String mealDetail; // 세부사항
    private String createAt; // create-At
    private String createAtTime; // create-At 시간 부분 추가
    private String updateAt; // update-At
}