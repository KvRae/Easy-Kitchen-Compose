package com.kvrae.easykitchen.data.mapper

import com.kvrae.easykitchen.data.remote.dto.IngredientResponse
import com.kvrae.easykitchen.data.local.entity.Ingredient as IngredientEntity

fun IngredientResponse.toEntity(): IngredientEntity = IngredientEntity(
    id = idResponse.orEmpty(),
    name = strIngredient,
    description = strDescription
)

fun IngredientEntity.toResponse(): IngredientResponse = IngredientResponse(
    idResponse = id,
    strDescription = description,
    strIngredient = name,
)
