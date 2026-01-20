package com.kvrae.easykitchen.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kvrae.easykitchen.data.local.entity.SavedMeal

@Dao
interface SavedMealDao {
    @Query("SELECT * FROM saved_meal")
    suspend fun getAllSavedMeals(): List<SavedMeal>

    @Query("SELECT * FROM saved_meal WHERE id = :id")
    suspend fun getSavedMealById(id: String): SavedMeal?

    @Query("DELETE FROM saved_meal")
    suspend fun deleteAllSavedMeals()

    @Query("DELETE FROM saved_meal WHERE id = :id")
    suspend fun deleteSavedMealById(id: String)

    @Upsert
    suspend fun saveMeal(savedMeal: SavedMeal)
}
