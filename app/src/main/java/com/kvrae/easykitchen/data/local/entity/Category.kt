package com.kvrae.easykitchen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey
    val id: String,
    val name: String? = null,
    val image: String? = null,
    val description: String? = null
)
