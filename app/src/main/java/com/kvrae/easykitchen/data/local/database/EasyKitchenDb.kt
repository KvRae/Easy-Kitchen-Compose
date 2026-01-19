package com.kvrae.easykitchen.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kvrae.easykitchen.data.local.converters.MealTypeConverters
import com.kvrae.easykitchen.data.local.dao.MealDao
import com.kvrae.easykitchen.data.local.dao.SavedMealDao
import com.kvrae.easykitchen.data.local.entity.Meal
import com.kvrae.easykitchen.data.local.entity.SavedMeal


@Database(entities = [SavedMeal::class, Meal::class], version = 1)
@TypeConverters(MealTypeConverters::class)
abstract class EasyKitchenDb: RoomDatabase() {
    abstract val savedMealDao: SavedMealDao
    abstract val mealDao: MealDao

    companion object {
        private const val DATABASE_NAME = "easy_kitchen_db"

        @Volatile
        private var INSTANCE: EasyKitchenDb? = null

        fun getInstance(context: Context): EasyKitchenDb {
            synchronized(this) {
                return INSTANCE ?: databaseBuilder(
                    context = context.applicationContext,
                    klass = EasyKitchenDb::class.java,
                    name = DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}
