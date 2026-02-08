package com.kvrae.easykitchen.domain.usecases

import com.kvrae.easykitchen.data.repository.SavedMealRepository

class DeleteSavedMealUseCase(private val repository: SavedMealRepository) {
    suspend operator fun invoke(id: String) = repository.deleteSavedMealById(id)
}
