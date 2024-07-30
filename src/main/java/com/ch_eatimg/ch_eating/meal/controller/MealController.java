package com.ch_eatimg.ch_eating.meal.controller;

import com.ch_eatimg.ch_eating.meal.dto.MealCreateRequestDto;
import com.ch_eatimg.ch_eating.meal.dto.MealDeleteRequestDto;
import com.ch_eatimg.ch_eating.meal.dto.MealModifyRequestDto;
import com.ch_eatimg.ch_eating.meal.service.MealServiceImpl;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/meal")
@RequiredArgsConstructor
public class MealController {

    private final MealServiceImpl mealService;

    // 식사량 등록
    @PostMapping
    private ResponseEntity<CustomApiResponse<?>> createMeal(@RequestBody MealCreateRequestDto dto) {
        return mealService.createMeals(dto);
    }

    // 삭사량 조회
    @GetMapping("/meals")
    private ResponseEntity<CustomApiResponse<?>> getMeals() { return mealService.getMeals(); }

    // 식사량 수정
    @PutMapping("/{mealId}")
    private ResponseEntity<CustomApiResponse<?>> updateMeal(@PathVariable Long mealId, @RequestBody MealModifyRequestDto.Req req) {
        return mealService.modifyMeal(mealId, req);
    }

    // 식사량 제거
    @DeleteMapping("/{mealId}")
    public ResponseEntity<CustomApiResponse<?>> deleteMeal(@PathVariable String mealId, @RequestBody MealDeleteRequestDto requestDto) {
        requestDto.setMealId(mealId);
        return mealService.deleteMeal(requestDto);
    }

}
