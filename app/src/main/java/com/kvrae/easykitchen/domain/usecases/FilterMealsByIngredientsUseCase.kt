package com.kvrae.easykitchen.domain.usecases

import com.kvrae.easykitchen.data.remote.dto.MealResponse

class FilterMealsByIngredientsUseCase {

    /**
     * Filters meals based on ingredient matches.
     * Returns a pair of lists: (exactMatches, partialMatches)
     * - exactMatches: meals that contain ALL selected ingredients
     * - partialMatches: meals that contain SOME selected ingredients (sorted by match count)
     */
    operator fun invoke(
        meals: List<MealResponse>,
        selectedIngredientNames: List<String>
    ): Pair<List<MealResponse>, List<MealResponse>> {
        if (selectedIngredientNames.isEmpty() || meals.isEmpty()) {
            return Pair(emptyList(), emptyList())
        }

        // Normalize ingredient names for case-insensitive comparison
        val normalizedSelectedIngredients = selectedIngredientNames
            .map { it.trim().lowercase() }
            .filter { it.isNotEmpty() }

        val mealsWithMatchCount = meals.map { meal ->
            val mealIngredients = extractMealIngredients(meal)
                .map { it.trim().lowercase() }

            val matchCount = normalizedSelectedIngredients.count { selectedIngredient ->
                mealIngredients.any { mealIngredient ->
                    mealIngredient.contains(selectedIngredient) ||
                            selectedIngredient.contains(mealIngredient)
                }
            }

            meal to matchCount
        }

        // Exact matches: meals containing all selected ingredients
        val exactMatches = mealsWithMatchCount
            .filter { (_, matchCount) -> matchCount == normalizedSelectedIngredients.size }
            .map { (meal, _) -> meal }

        // Partial matches: meals containing some (but not all) selected ingredients
        val partialMatches = mealsWithMatchCount
            .filter { (_, matchCount) ->
                matchCount > 0 && matchCount < normalizedSelectedIngredients.size
            }
            .sortedByDescending { (_, matchCount) -> matchCount }
            .map { (meal, _) -> meal }

        return Pair(exactMatches, partialMatches)
    }

    private fun extractMealIngredients(meal: MealResponse): List<String> {
        return listOfNotNull(
            meal.strIngredient1,
            meal.strIngredient2,
            meal.strIngredient3,
            meal.strIngredient4,
            meal.strIngredient5,
            meal.strIngredient6,
            meal.strIngredient7,
            meal.strIngredient8,
            meal.strIngredient9,
            meal.strIngredient10,
            meal.strIngredient11,
            meal.strIngredient12,
            meal.strIngredient13,
            meal.strIngredient14,
            meal.strIngredient15,
            meal.strIngredient16,
            meal.strIngredient17,
            meal.strIngredient18,
            meal.strIngredient19,
            meal.strIngredient20
        ).filter { it.isNotBlank() }
    }
}
