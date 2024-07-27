package com.ch_eatimg.ch_eating.meal.service;

import com.ch_eatimg.ch_eating.meal.dto.MealCreateRequestDto;
import com.ch_eatimg.ch_eating.meal.dto.MealDeleteRequestDto;
import com.ch_eatimg.ch_eating.meal.dto.MealUpdateRequestDto;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MealService {
    ResponseEntity<CustomApiResponse<?>> createMeals(MealCreateRequestDto requestDto);
    //ResponseEntity<CustomApiResponse<?>> updateMeal(String userId, Long mealId, MealUpdateRequestDto.Req req);
    //ResponseEntity<CustomApiResponse<?>> deleteMeal(MealDeleteRequestDto dto);
}