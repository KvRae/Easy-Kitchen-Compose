package com.kvrae.easykitchen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kvrae.easykitchen.data.remote.dto.Meal
import com.kvrae.easykitchen.data.remote.dto.MealResponse

@Entity(tableName = "saved_meal")
data class SavedMeal(
    @PrimaryKey(autoGenerate = true)
    val idTable: Int = 0,
    val id : String? = null,
    val name: String? = null,
    val source: String? = null,
    val category: String? = null,
    val area: String? = null,
    val image: String? = null,
    val instructions: String? = null,
    val youtube: String? = null,
    val ingredients: List<String> = emptyList(),
    val measures: List<String> = emptyList(),
    val isFavorite: Boolean = false
)

fun Meal.asSavedMeal() : SavedMeal {
    return SavedMeal(
        id = this.id,
        name = this.name,
        source = this.source,
        category = this.category,
        area = this.area,
        image = this.image,
        instructions = this.instructions,
        youtube = this.youtube,
        ingredients = this.ingredients,
        measures = this.measures
    )
}

fun MealResponse.toSavedMeal(): SavedMeal = SavedMeal(
    id = idResponse,
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
    ).filter { it.isNotBlank() },
    isFavorite = true
)
