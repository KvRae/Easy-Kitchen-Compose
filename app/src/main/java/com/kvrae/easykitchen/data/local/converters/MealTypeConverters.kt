package com.kvrae.easykitchen.data.local.converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class MealTypeConverters {
    @TypeConverter
    fun fromList(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return Json.decodeFromString(value)
    }
}
