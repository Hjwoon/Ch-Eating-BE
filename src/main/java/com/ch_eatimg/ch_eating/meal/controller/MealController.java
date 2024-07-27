package com.ch_eatimg.ch_eating.meal.controller;

import com.ch_eatimg.ch_eating.meal.dto.MealCreateRequestDto;
import com.ch_eatimg.ch_eating.meal.dto.MealUpdateRequestDto;
import com.ch_eatimg.ch_eating.meal.dto.MealDeleteRequestDto;
import com.ch_eatimg.ch_eating.meal.service.MealServiceImpl;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ch-eating/meal")
@RequiredArgsConstructor
public class MealController {

    private final MealServiceImpl mealService;

    @PostMapping
    private ResponseEntity<CustomApiResponse<?>> createMeal(@RequestBody MealCreateRequestDto dto) {
        return mealService.createMeals(dto);
    }

    /*@PutMapping("/{mealId}")
    private ResponseEntity<CustomApiResponse<?>> updateMeal(@PathVariable String userId, @PathVariable Long mealId, @RequestBody MealUpdateRequestDto.Req req) {
        return mealService.updateMeal(userId, mealId, req);
    }*/

    /*@DeleteMapping
    private ResponseEntity<CustomApiResponse<?>> deleteMeal(@RequestBody @Valid MealDeleteRequestDto dto) {
        return mealService.deleteMeal(dto);
    }*/
}
