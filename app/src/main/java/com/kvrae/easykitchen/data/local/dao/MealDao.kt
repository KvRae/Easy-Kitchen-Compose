package com.kvrae.easykitchen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kvrae.easykitchen.data.local.entity.Meal

@Dao
interface MealDao {
    @Query("SELECT * FROM meal")
    suspend fun getAllMeals(): List<Meal>

    @Query("SELECT * FROM meal WHERE idResponse = :mealId LIMIT 1")
    suspend fun getMealById(mealId: String): Meal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(meals: List<Meal>)

    @Query("DELETE FROM meal")
    suspend fun deleteAllMeals()

    @Query("SELECT COUNT(*) FROM meal")
    suspend fun getMealCount(): Int
}
