package com.kvrae.easykitchen.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    @SerialName("_id")
    val idResponse: String?,
    @SerialName("strCategory")
    val strCategory: String?,
    @SerialName("strCategoryDescription")
    val strCategoryDescription: String?,
    @SerialName("strCategoryThumb")
    val strCategoryThumb: String?,
)

@Serializable
data class CategoryErrorResponse(
    val message: String? = null,
    val error: String? = null
)

@Serializable
data class CategoryApiResponse(
    val message: String? = null,
    val count: Int? = null,
    val data: List<CategoryResponse>? = null
)

data class Category(
    val id: String? = null,
    val name: String? = null,
    val image: String? = null,
    val description: String? = null,
)

fun CategoryResponse.asDto(): Category =
    Category(
        id = idResponse,
        name = strCategory,
        image = strCategoryThumb,
    )

