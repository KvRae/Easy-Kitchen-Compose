package com.kvrae.easykitchen.data.repository

import com.kvrae.easykitchen.data.local.dao.SavedMealDao
import com.kvrae.easykitchen.data.local.entity.SavedMeal

class SavedMealRepository(
    private val savedMealDao: SavedMealDao
) {
    suspend fun saveMeal(savedMeal: SavedMeal) {
        // savedMealDao.saveMeal(savedMeal)
    }

    suspend fun getAllSavedMeals(): List<SavedMeal> {
        return emptyList() // savedMealDao.getAllSavedMeals()
    }

    suspend fun getSavedMealById(id: String): SavedMeal? {
        return null // savedMealDao.getSavedMealById(id)
    }

    suspend fun deleteSavedMealById(id: String) {
        // savedMealDao.deleteSavedMealById(id)
    }

}