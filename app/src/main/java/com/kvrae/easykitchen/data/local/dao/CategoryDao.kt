package com.kvrae.easykitchen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kvrae.easykitchen.data.local.entity.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    suspend fun getAll(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<Category>)

    @Query("DELETE FROM category")
    suspend fun clear()
}
