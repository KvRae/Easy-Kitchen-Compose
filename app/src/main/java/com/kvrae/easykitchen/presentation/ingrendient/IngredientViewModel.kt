package com.kvrae.easykitchen.presentation.ingrendient

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvrae.easykitchen.data.remote.dto.IngredientResponse
import com.kvrae.easykitchen.domain.usecases.GetIngredientsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IngredientViewModel(private val getIngredientsUseCase: GetIngredientsUseCase) : ViewModel() {
    private val _ingredientsState = MutableStateFlow<IngredientState>(IngredientState.Loading)
    val ingredientsState: StateFlow<IngredientState> = _ingredientsState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _filteredIngredientsState =
        MutableStateFlow<IngredientState>(IngredientState.Loading)
    val filteredIngredientsState: StateFlow<IngredientState> = _filteredIngredientsState

    private val ingredientsBasket = mutableStateListOf<IngredientResponse>()

    init {
        getIngredients()
    }

    fun getIngredients() {
        _ingredientsState.value = IngredientState.Loading
        viewModelScope.launch {
            val result = getIngredientsUseCase()
            _ingredientsState.value = when {
                result.isSuccess -> {
                    val ingredients = result.getOrNull()!!
                    _filteredIngredientsState.value = IngredientState.Success(ingredients)
                    IngredientState.Success(ingredients)
                }
                result.isFailure -> {
                    val error = IngredientState.Error(
                        result.exceptionOrNull()?.message ?: "Failed to load data"
                    )
                    _filteredIngredientsState.value = error
                    error
                }
                else -> {
                    val error = IngredientState.Error("Unknown error")
                    _filteredIngredientsState.value = error
                    error
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterIngredients(query)
    }

    private fun filterIngredients(query: String) {
        val currentState = _ingredientsState.value
        if (currentState is IngredientState.Success) {
            val allIngredients = currentState.ingredients

            if (query.isBlank()) {
                _filteredIngredientsState.value = IngredientState.Success(allIngredients)
            } else {
                val filtered = allIngredients.filter { ingredient ->
                    ingredient.strIngredient?.contains(query, ignoreCase = true) == true ||
                            ingredient.strDescription?.contains(query, ignoreCase = true) == true
                }
                _filteredIngredientsState.value = IngredientState.Success(filtered)
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        val currentState = _ingredientsState.value
        if (currentState is IngredientState.Success) {
            _filteredIngredientsState.value = currentState
        }
    }


    fun updateIngredientInBasket(ingredient: IngredientResponse) {
        if (ingredientsBasket.contains(ingredient)) {
            ingredientsBasket.remove(ingredient)
        } else {
            ingredientsBasket.add(ingredient)
        }
    }

    fun isIngredientInBasket(ingredient: IngredientResponse): Boolean {
        return ingredientsBasket.contains(ingredient)
    }
}

sealed class IngredientState {
    data object Loading : IngredientState()
    data class Success(val ingredients: List<IngredientResponse>) : IngredientState()
    data class Error(val message: String) : IngredientState()
}
