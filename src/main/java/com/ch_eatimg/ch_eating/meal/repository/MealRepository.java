package com.ch_eatimg.ch_eating.meal.repository;

import com.ch_eatimg.ch_eating.domain.Meal;
import com.ch_eatimg.ch_eating.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByUserUserId(String userId);
    Optional<Meal> findByMealId(Long mealId);
}
