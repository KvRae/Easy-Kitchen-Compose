package com.kvrae.easykitchen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kvrae.easykitchen.data.local.entity.Ingredient

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredient")
    suspend fun getAll(): List<Ingredient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ingredients: List<Ingredient>)

    @Query("DELETE FROM ingredient")
    suspend fun clear()
}
