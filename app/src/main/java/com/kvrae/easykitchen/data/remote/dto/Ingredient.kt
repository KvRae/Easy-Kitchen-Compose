package com.kvrae.easykitchen.data.remote.dto

import com.kvrae.easykitchen.utils.INGREDIENT_IMAGE_URL
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IngredientResponse(
    @SerialName("_id")
    val idResponse: String?,
    @SerialName("strDescription")
    val strDescription: String?,
    @SerialName("strIngredient")
    val strIngredient: String?,
)

@Serializable
data class IngredientApiResponse(
    val message: String? = null,
    val count: Int? = null,
    val data: List<IngredientResponse>? = null,
    val error: String? = null
)

@Serializable
data class IngredientErrorResponse(
    val message: String? = null,
    val error: String? = null
)

data class Ingredient(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val image: String? = "$INGREDIENT_IMAGE_URL$name.png"
)

fun IngredientResponse.asDto() =
    Ingredient(
        id = idResponse,
        name = strIngredient,
        description = strDescription
    )

