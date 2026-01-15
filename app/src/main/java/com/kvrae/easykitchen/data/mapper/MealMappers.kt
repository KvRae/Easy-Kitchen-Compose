package com.kvrae.easykitchen.data.mapper

import com.kvrae.easykitchen.data.remote.dto.MealResponse
import com.kvrae.easykitchen.data.local.entity.Meal as MealEntity

fun MealResponse.toEntity(): MealEntity {
    return MealEntity(
        idResponse = idResponse ?: "",
        name = strMeal,
        source = strSource,
        category = strCategory,
        area = strArea,
        image = strMealThumb,
        instructions = strInstructions,
        youtube = strYoutube,
        ingredients = listOfNotNull(
            strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
            strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
            strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
            strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20
        ).filter { it.isNotBlank() },
        measures = listOfNotNull(
            strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
            strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
            strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
            strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20
        ).filter { it.isNotBlank() }
    )
}

fun MealEntity.toResponse(): MealResponse {
    return MealResponse(
        idResponse = idResponse,
        strMeal = name,
        strSource = source,
        strCategory = category,
        strArea = area,
        strMealThumb = image,
        strInstructions = instructions,
        strYoutube = youtube,
        // Mapping back ingredients and measures simplified for this response DTO
        strIngredient1 = ingredients.getOrNull(0),
        strIngredient2 = ingredients.getOrNull(1),
        strMeasure1 = measures.getOrNull(0),
        strMeasure2 = measures.getOrNull(1),
        // ... add others if needed by the UI, but usually UI uses Domain models
        strIngredient3 = null, strIngredient4 = null, strIngredient5 = null,
        strIngredient6 = null, strIngredient7 = null, strIngredient8 = null,
        strIngredient9 = null, strIngredient10 = null, strIngredient11 = null,
        strIngredient12 = null, strIngredient13 = null, strIngredient14 = null,
        strIngredient15 = null, strIngredient16 = null, strIngredient17 = null,
        strIngredient18 = null, strIngredient19 = null, strIngredient20 = null,
        strMeasure3 = null, strMeasure4 = null, strMeasure5 = null,
        strMeasure6 = null, strMeasure7 = null, strMeasure8 = null,
        strMeasure9 = null, strMeasure10 = null, strMeasure11 = null,
        strMeasure12 = null, strMeasure13 = null, strMeasure14 = null,
        strMeasure15 = null, strMeasure16 = null, strMeasure17 = null,
        strMeasure18 = null, strMeasure19 = null, strMeasure20 = null,
        strImageSource = null
    )
}
