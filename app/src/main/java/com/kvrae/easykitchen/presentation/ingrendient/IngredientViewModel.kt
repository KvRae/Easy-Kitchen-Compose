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

    private val _basketCount = MutableStateFlow(0)
    val basketCount: StateFlow<Int> = _basketCount

    private val _basketItems = MutableStateFlow<List<IngredientResponse>>(emptyList())
    val basketItems: StateFlow<List<IngredientResponse>> = _basketItems

    private var isDataLoaded = false

    init {
        getIngredients()
    }

    fun getIngredients() {
        viewModelScope.launch {
            _ingredientsState.value = IngredientState.Loading
            try {
                val result = getIngredientsUseCase()
                if (result.isSuccess) {
                    val ingredients = result.getOrNull() ?: emptyList()

                    if (ingredients.isEmpty()) {
                        _ingredientsState.value =
                            IngredientState.Error("No ingredients available at the moment. Please try again later.")
                        _filteredIngredientsState.value =
                            IngredientState.Error("No ingredients available at the moment. Please try again later.")
                    } else {
                        isDataLoaded = true
                        _ingredientsState.value = IngredientState.Success(ingredients)
                        // Re-apply search filter if there's an active query
                        filterIngredients(_searchQuery.value)
                    }
                } else {
                    val exception = result.exceptionOrNull()
                    val errorMessage = when {
                        exception?.message?.contains("timeout", ignoreCase = true) == true ->
                            "Connection timeout. Please check your internet and try again."

                        exception?.message?.contains(
                            "Unable to resolve host",
                            ignoreCase = true
                        ) == true ->
                            "No internet connection. Please check your network settings."

                        exception?.message?.contains("500", ignoreCase = true) == true ->
                            "Server is temporarily unavailable. Please try again later."

                        exception?.message?.contains(
                            "unexpected format",
                            ignoreCase = true
                        ) == true ->
                            "Unable to load ingredients. Please try again later."

                        else ->
                            "Unable to load ingredients. Please check your connection and try again."
                    }

                    // If we have data, don't show error screen, just stay on current state
                    if (!isDataLoaded) {
                        val error = IngredientState.Error(errorMessage)
                        _ingredientsState.value = error
                        _filteredIngredientsState.value = error
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("IngredientViewModel", "Error loading ingredients", e)
                if (!isDataLoaded) {
                    val errorMessage = when {
                        e.message?.contains("timeout", ignoreCase = true) == true ->
                            "Connection timeout. Please check your internet and try again."

                        e.message?.contains("Unable to resolve host", ignoreCase = true) == true ->
                            "No internet connection. Please check your network settings."

                        else ->
                            "An unexpected error occurred. Please try again."
                    }
                    val error = IngredientState.Error(errorMessage)
                    _ingredientsState.value = error
                    _filteredIngredientsState.value = error
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
        if (ingredientsBasket.any { it.idResponse == ingredient.idResponse }) {
            ingredientsBasket.removeAll { it.idResponse == ingredient.idResponse }
        } else {
            ingredientsBasket.add(ingredient)
        }
        _basketCount.value = ingredientsBasket.size
        _basketItems.value = ingredientsBasket.toList()
    }

    fun isIngredientInBasket(ingredient: IngredientResponse): Boolean {
        return ingredientsBasket.any { it.idResponse == ingredient.idResponse }
    }

    fun getSelectedIngredientNames(): List<String> {
        return ingredientsBasket.mapNotNull { it.strIngredient }
    }

    fun getBasketIngredients(): List<IngredientResponse> {
        return ingredientsBasket.toList()
    }
}

sealed class IngredientState {
    data object Loading : IngredientState()
    data class Success(val ingredients: List<IngredientResponse>) : IngredientState()
    data class Error(val message: String) : IngredientState()
}
