package com.kvrae.easykitchen.domain.usecases

import com.kvrae.easykitchen.data.local.entity.SavedMeal
import com.kvrae.easykitchen.data.repository.SavedMealRepository

class SaveMealUseCase(private val repository: SavedMealRepository) {
    suspend operator fun invoke(savedMeal: SavedMeal) = repository.saveMeal(savedMeal)
}
