package com.ch_eatimg.ch_eating.meal.repository;

import com.ch_eatimg.ch_eating.domain.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {
    Optional<Meal> findByMealId(Long mealId);
}
