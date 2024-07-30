package com.ch_eatimg.ch_eating.meal.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MealModifyResponseDto {
    private Long mealId;
    private String userId;
    private String mealType;
    private String mealBrand;
    private String mealName;
    private String mealAmount;
    private String mealDetail;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}