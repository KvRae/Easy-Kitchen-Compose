package com.kvrae.easykitchen.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MealResponse(
    @SerialName("_id")
    val idResponse: String?,
    @SerialName("strArea")
    val strArea: String?,
    @SerialName("strCategory")
    val strCategory: String?,
    @SerialName("strImageSource")
    val strImageSource: String?,
    @SerialName("strIngredient1")
    val strIngredient1: String?,
    @SerialName("strIngredient10")
    val strIngredient10: String?,
    @SerialName("strIngredient11")
    val strIngredient11: String?,
    @SerialName("strIngredient12")
    val strIngredient12: String?,
    @SerialName("strIngredient13")
    val strIngredient13: String?,
    @SerialName("strIngredient14")
    val strIngredient14: String?,
    @SerialName("strIngredient15")
    val strIngredient15: String?,
    @SerialName("strIngredient16")
    val strIngredient16: String?,
    @SerialName("strIngredient17")
    val strIngredient17: String?,
    @SerialName("strIngredient18")
    val strIngredient18: String?,
    @SerialName("strIngredient19")
    val strIngredient19: String?,
    @SerialName("strIngredient2")
    val strIngredient2: String?,
    @SerialName("strIngredient20")
    val strIngredient20: String?,
    @SerialName("strIngredient3")
    val strIngredient3: String?,
    @SerialName("strIngredient4")
    val strIngredient4: String?,
    @SerialName("strIngredient5")
    val strIngredient5: String?,
    @SerialName("strIngredient6")
    val strIngredient6: String?,
    @SerialName("strIngredient7")
    val strIngredient7: String?,
    @SerialName("strIngredient8")
    val strIngredient8: String?,
    @SerialName("strIngredient9")
    val strIngredient9: String?,
    @SerialName("strInstructions")
    val strInstructions: String?,
    @SerialName("strMeal")
    val strMeal: String?,
    @SerialName("strMealThumb")
    val strMealThumb: String?,
    @SerialName("strMeasure1")
    val strMeasure1: String?,
    @SerialName("strMeasure10")
    val strMeasure10: String?,
    @SerialName("strMeasure11")
    val strMeasure11: String?,
    @SerialName("strMeasure12")
    val strMeasure12: String?,
    @SerialName("strMeasure13")
    val strMeasure13: String?,
    @SerialName("strMeasure14")
    val strMeasure14: String?,
    @SerialName("strMeasure15")
    val strMeasure15: String?,
    @SerialName("strMeasure16")
    val strMeasure16: String?,
    @SerialName("strMeasure17")
    val strMeasure17: String?,
    @SerialName("strMeasure18")
    val strMeasure18: String?,
    @SerialName("strMeasure19")
    val strMeasure19: String?,
    @SerialName("strMeasure2")
    val strMeasure2: String?,
    @SerialName("strMeasure20")
    val strMeasure20: String?,
    @SerialName("strMeasure3")
    val strMeasure3: String?,
    @SerialName("strMeasure4")
    val strMeasure4: String?,
    @SerialName("strMeasure5")
    val strMeasure5: String?,
    @SerialName("strMeasure6")
    val strMeasure6: String?,
    @SerialName("strMeasure7")
    val strMeasure7: String?,
    @SerialName("strMeasure8")
    val strMeasure8: String?,
    @SerialName("strMeasure9")
    val strMeasure9: String?,
    @SerialName("strSource")
    val strSource: String?,
    @SerialName("strYoutube")
    val strYoutube: String?,
)



data class Meal(
    val id: String? = null,
    val name: String? = null,
    val source: String? = null,
    val category: String? = null,
    val area: String? = null,
    val image: String? = null,
    val instructions: String? = null,
    val youtube: String? = null,
    val ingredients: List<String> = emptyList(),
    val measures: List<String> = emptyList(),
)

data class MealDetail(
    val id: String? = null,
    val name: String? = null,
    val category: String? = null,
    val area: String? = null,
    val image: String? = null,
    val instructions: String? = null,
    val youtube: String? = null,
    val source: String? = null,
    val ingredients: List<String> = emptyList(),
    val measures: List<String> = emptyList(),
)

fun MealResponse.asDto(): Meal =
    Meal(
        id = idResponse,
        name = strMeal,
        category = strCategory,
        source = strSource,
        area = strArea,
        image = strMealThumb,
    )

fun MealResponse.asMealDetail(): MealDetail =
    MealDetail(
        id = idResponse,
        name = strMeal,
        category = strCategory,
        area = strArea,
        image = strMealThumb,
        instructions = strInstructions,
        youtube = strYoutube,
        source = strSource,
        ingredients =
        listOf(
            strIngredient1.orEmpty().trim(),
            strIngredient2.orEmpty().trim(),
            strIngredient3.orEmpty().trim(),
            strIngredient4.orEmpty().trim(),
            strIngredient5.orEmpty().trim(),
            strIngredient6.orEmpty().trim(),
            strIngredient7.orEmpty().trim(),
            strIngredient8.orEmpty().trim(),
            strIngredient9.orEmpty().trim(),
            strIngredient10.orEmpty().trim(),
            strIngredient11.orEmpty().trim(),
            strIngredient12.orEmpty().trim(),
            strIngredient13.orEmpty().trim(),
            strIngredient14.orEmpty().trim(),
            strIngredient15.orEmpty().trim(),
            strIngredient16.orEmpty().trim(),
            strIngredient17.orEmpty().trim(),
            strIngredient18.orEmpty().trim(),
            strIngredient19.orEmpty().trim(),
            strIngredient20.orEmpty().trim(),
        ).filter { it.isNotEmpty() },
        measures =
        listOf(
            strMeasure1.orEmpty().trim(),
            strMeasure2.orEmpty().trim(),
            strMeasure3.orEmpty().trim(),
            strMeasure4.orEmpty().trim(),
            strMeasure5.orEmpty().trim(),
            strMeasure6.orEmpty().trim(),
            strMeasure7.orEmpty().trim(),
            strMeasure8.orEmpty().trim(),
            strMeasure9.orEmpty().trim(),
            strMeasure10.orEmpty().trim(),
            strMeasure11.orEmpty().trim(),
            strMeasure12.orEmpty().trim(),
            strMeasure13.orEmpty().trim(),
            strMeasure14.orEmpty().trim(),
            strMeasure15.orEmpty().trim(),
            strMeasure16.orEmpty().trim(),
            strMeasure17.orEmpty().trim(),
            strMeasure18.orEmpty().trim(),
            strMeasure19.orEmpty().trim(),
            strMeasure20.orEmpty().trim(),
        ).filter { it.isNotEmpty() },
    )

@Serializable
data class MealErrorResponse(
    val message: String? = null,
    val error: String? = null
)

@Serializable
data class MealApiResponse(
    val message: String? = null,
    val count: Int? = null,
    val data: List<MealResponse>? = null,
    val error: String? = null
)
