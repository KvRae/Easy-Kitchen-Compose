package com.kvrae.easykitchen.presentation.filtered_meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvrae.easykitchen.data.remote.dto.MealResponse
import com.kvrae.easykitchen.domain.usecases.FilterMealsByIngredientsUseCase
import com.kvrae.easykitchen.domain.usecases.GetMealsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FilteredMealsViewModel(
    private val getMealsUseCase: GetMealsUseCase,
    private val filterMealsByIngredientsUseCase: FilterMealsByIngredientsUseCase
) : ViewModel() {

    private val _filteredMealsState =
        MutableStateFlow<FilteredMealsState>(FilteredMealsState.Loading)
    val filteredMealsState: StateFlow<FilteredMealsState> = _filteredMealsState

    private val _exactMatches = MutableStateFlow<List<MealResponse>>(emptyList())
    val exactMatches: StateFlow<List<MealResponse>> = _exactMatches

    private val _partialMatches = MutableStateFlow<List<MealResponse>>(emptyList())
    val partialMatches: StateFlow<List<MealResponse>> = _partialMatches

    fun filterMealsByIngredients(selectedIngredientNames: List<String>) {
        if (selectedIngredientNames.isEmpty()) {
            _filteredMealsState.value = FilteredMealsState.Error("No ingredients selected")
            return
        }

        viewModelScope.launch {
            _filteredMealsState.value = FilteredMealsState.Loading

            try {
                val result = getMealsUseCase()

                result.fold(
                    onSuccess = { meals ->
                        val (exact, partial) = filterMealsByIngredientsUseCase(
                            meals = meals,
                            selectedIngredientNames = selectedIngredientNames
                        )

                        _exactMatches.value = exact
                        _partialMatches.value = partial

                        if (exact.isEmpty() && partial.isEmpty()) {
                            _filteredMealsState.value = FilteredMealsState.Error(
                                "No meals found with the selected ingredients"
                            )
                        } else {
                            _filteredMealsState.value = FilteredMealsState.Success(
                                exactMatches = exact,
                                partialMatches = partial
                            )
                        }
                    },
                    onFailure = { error ->
                        _filteredMealsState.value = FilteredMealsState.Error(
                            error.message ?: "Failed to load meals"
                        )
                    }
                )
            } catch (e: Exception) {
                _filteredMealsState.value = FilteredMealsState.Error(
                    e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    fun filterMealsByCategory(categoryName: String) {
        if (categoryName.isBlank()) {
            _filteredMealsState.value = FilteredMealsState.Error("Category not provided")
            return
        }
        viewModelScope.launch {
            _filteredMealsState.value = FilteredMealsState.Loading
            try {
                val result = getMealsUseCase()
                result.fold(
                    onSuccess = { meals ->
                        val matches = meals.filter {
                            it.strCategory.equals(categoryName, ignoreCase = true)
                        }
                        _exactMatches.value = matches
                        _partialMatches.value = emptyList()
                        if (matches.isEmpty()) {
                            _filteredMealsState.value = FilteredMealsState.Error(
                                "No meals found in $categoryName"
                            )
                        } else {
                            _filteredMealsState.value = FilteredMealsState.Success(
                                exactMatches = matches,
                                partialMatches = emptyList()
                            )
                        }
                    },
                    onFailure = { error ->
                        _filteredMealsState.value = FilteredMealsState.Error(
                            error.message ?: "Failed to load meals"
                        )
                    }
                )
            } catch (e: Exception) {
                _filteredMealsState.value = FilteredMealsState.Error(
                    e.message ?: "An unexpected error occurred"
                )
            }
        }
    }
}

sealed class FilteredMealsState {
    data object Loading : FilteredMealsState()
    data class Success(
        val exactMatches: List<MealResponse>,
        val partialMatches: List<MealResponse>
    ) : FilteredMealsState()

    data class Error(val message: String) : FilteredMealsState()
}
