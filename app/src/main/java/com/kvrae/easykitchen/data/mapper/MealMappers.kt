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
        // Map all 20 ingredients
        strIngredient1 = ingredients.getOrNull(0),
        strIngredient2 = ingredients.getOrNull(1),
        strIngredient3 = ingredients.getOrNull(2),
        strIngredient4 = ingredients.getOrNull(3),
        strIngredient5 = ingredients.getOrNull(4),
        strIngredient6 = ingredients.getOrNull(5),
        strIngredient7 = ingredients.getOrNull(6),
        strIngredient8 = ingredients.getOrNull(7),
        strIngredient9 = ingredients.getOrNull(8),
        strIngredient10 = ingredients.getOrNull(9),
        strIngredient11 = ingredients.getOrNull(10),
        strIngredient12 = ingredients.getOrNull(11),
        strIngredient13 = ingredients.getOrNull(12),
        strIngredient14 = ingredients.getOrNull(13),
        strIngredient15 = ingredients.getOrNull(14),
        strIngredient16 = ingredients.getOrNull(15),
        strIngredient17 = ingredients.getOrNull(16),
        strIngredient18 = ingredients.getOrNull(17),
        strIngredient19 = ingredients.getOrNull(18),
        strIngredient20 = ingredients.getOrNull(19),
        // Map all 20 measures
        strMeasure1 = measures.getOrNull(0),
        strMeasure2 = measures.getOrNull(1),
        strMeasure3 = measures.getOrNull(2),
        strMeasure4 = measures.getOrNull(3),
        strMeasure5 = measures.getOrNull(4),
        strMeasure6 = measures.getOrNull(5),
        strMeasure7 = measures.getOrNull(6),
        strMeasure8 = measures.getOrNull(7),
        strMeasure9 = measures.getOrNull(8),
        strMeasure10 = measures.getOrNull(9),
        strMeasure11 = measures.getOrNull(10),
        strMeasure12 = measures.getOrNull(11),
        strMeasure13 = measures.getOrNull(12),
        strMeasure14 = measures.getOrNull(13),
        strMeasure15 = measures.getOrNull(14),
        strMeasure16 = measures.getOrNull(15),
        strMeasure17 = measures.getOrNull(16),
        strMeasure18 = measures.getOrNull(17),
        strMeasure19 = measures.getOrNull(18),
        strMeasure20 = measures.getOrNull(19),
        strImageSource = null
    )
}
