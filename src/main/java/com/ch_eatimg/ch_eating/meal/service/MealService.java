package com.ch_eatimg.ch_eating.meal.service;

import com.ch_eatimg.ch_eating.meal.dto.MealCreateRequestDto;
import com.ch_eatimg.ch_eating.meal.dto.MealDeleteRequestDto;
import com.ch_eatimg.ch_eating.meal.dto.MealModifyRequestDto;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

public interface MealService {
    ResponseEntity<CustomApiResponse<?>> createMeals(MealCreateRequestDto requestDto);
    ResponseEntity<CustomApiResponse<?>> getMeals(String userId);
    ResponseEntity<CustomApiResponse<?>> modifyMeal(Long mealId, MealModifyRequestDto.Req req);
    ResponseEntity<CustomApiResponse<?>> deleteMeal(MealDeleteRequestDto dto);
}