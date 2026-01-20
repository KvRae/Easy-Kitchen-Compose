package com.kvrae.easykitchen.data.mapper

import com.kvrae.easykitchen.data.remote.dto.CategoryResponse
import com.kvrae.easykitchen.data.local.entity.Category as CategoryEntity

fun CategoryResponse.toEntity(): CategoryEntity = CategoryEntity(
    id = idResponse.orEmpty(),
    name = strCategory,
    image = strCategoryThumb,
    description = strCategoryDescription
)

fun CategoryEntity.toResponse(): CategoryResponse = CategoryResponse(
    idResponse = id,
    strCategory = name,
    strCategoryDescription = description,
    strCategoryThumb = image
)
