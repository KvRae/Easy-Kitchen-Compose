package com.kvrae.easykitchen.domain.usecases

import com.kvrae.easykitchen.data.remote.dto.MealResponse

/**
 * Use case for filtering meals by cuisine area
 */
class FilterMealsByAreaUseCase {
    operator fun invoke(meals: List<MealResponse>, area: String): List<MealResponse> {
        return meals.filter { meal ->
            meal.strArea?.equals(area, ignoreCase = true) == true
        }
    }
}

