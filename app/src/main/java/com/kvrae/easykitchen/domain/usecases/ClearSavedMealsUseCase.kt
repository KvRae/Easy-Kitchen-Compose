package com.kvrae.easykitchen.domain.usecases

import com.kvrae.easykitchen.data.repository.SavedMealRepository

class ClearSavedMealsUseCase(private val repository: SavedMealRepository) {
    suspend operator fun invoke() = repository.clearAll()
}
