package com.kvrae.easykitchen.domain.usecases

import com.kvrae.easykitchen.data.local.entity.SavedMeal
import com.kvrae.easykitchen.data.repository.SavedMealRepository

class GetSavedMealsUseCase(private val repository: SavedMealRepository) {
    suspend operator fun invoke(): List<SavedMeal> = repository.getAllSavedMeals()
}
