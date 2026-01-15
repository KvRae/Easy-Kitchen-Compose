package com.kvrae.easykitchen.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kvrae.easykitchen.data.local.converters.MealTypeConverters
import com.kvrae.easykitchen.data.local.dao.MealDao
import com.kvrae.easykitchen.data.local.dao.SavedMealDao
import com.kvrae.easykitchen.data.local.entity.Meal
import com.kvrae.easykitchen.data.local.entity.SavedMeal


@Database(entities = [SavedMeal::class, Meal::class], version = 2)
@TypeConverters(MealTypeConverters::class)
abstract class EasyKitchenDb: RoomDatabase() {
    abstract val savedMealDao: SavedMealDao
    abstract val mealDao: MealDao

    companion object {
        private const val DATABASE_NAME = "easy_kitchen_db"

        @Volatile
        private var INSTANCE: EasyKitchenDb? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `meal` (
                        `idTable` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        `idResponse` TEXT NOT NULL, 
                        `name` TEXT, 
                        `source` TEXT, 
                        `category` TEXT, 
                        `area` TEXT, 
                        `image` TEXT, 
                        `instructions` TEXT, 
                        `youtube` TEXT, 
                        `ingredients` TEXT NOT NULL, 
                        `measures` TEXT NOT NULL, 
                        `isFavorite` INTEGER NOT NULL DEFAULT 0
                    )
                    """.trimIndent()
                )
            }
        }

        fun getInstance(context: Context): EasyKitchenDb {
            synchronized(this) {
                return INSTANCE ?: databaseBuilder(
                    context = context.applicationContext,
                    klass = EasyKitchenDb::class.java,
                    name = DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration() // Force recreation if migration fails
                    .build().also { INSTANCE = it }
            }
        }
    }
}
