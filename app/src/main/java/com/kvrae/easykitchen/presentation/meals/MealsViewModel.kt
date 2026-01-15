package com.kvrae.easykitchen.presentation.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvrae.easykitchen.data.remote.dto.MealResponse
import com.kvrae.easykitchen.data.remote.dto.asDto
import com.kvrae.easykitchen.domain.model.FilterOptions
import com.kvrae.easykitchen.domain.model.MealFilter
import com.kvrae.easykitchen.domain.model.SortOption
import com.kvrae.easykitchen.domain.usecases.GetMealsUseCase
import com.kvrae.easykitchen.utils.getMealCategoryByTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MealsViewModel(
    private val getMealsUseCase: GetMealsUseCase,
) : ViewModel() {

    private val _mealState = MutableStateFlow<MealState>(MealState.Loading)
    val mealState : StateFlow<MealState> = _mealState

    private val _meals = MutableStateFlow<List<MealResponse>>(emptyList())
    val meals: StateFlow<List<MealResponse>> = _meals

    private val _filteredMeals = MutableStateFlow<List<MealResponse>>(emptyList())
    val filteredMeals: StateFlow<List<MealResponse>> = _filteredMeals

    private val _currentFilter = MutableStateFlow(MealFilter())
    val currentFilter: StateFlow<MealFilter> = _currentFilter

    private val _filterOptions = MutableStateFlow(FilterOptions())
    val filterOptions: StateFlow<FilterOptions> = _filterOptions

    private val _mealsByTime = MutableStateFlow<List<MealResponse>>(emptyList())
    val mealsByTime: StateFlow<List<MealResponse>> = _mealsByTime

    init {
        fetchMeals()
    }

    fun fetchMeals() {
        viewModelScope.launch {
            _mealState.value = MealState.Loading
            val result = getMealsUseCase()
            result.fold(
                onSuccess = { data ->
                    _meals.value = data
                    _filteredMeals.value = data
                    updateFilterOptions(data)
                    _mealState.value = MealState.Success(data)
                    applyFilter(_currentFilter.value)
                },
                onFailure = { error ->
                    _meals.value = emptyList()
                    _filteredMeals.value = emptyList()
                    _mealState.value = MealState.Error(error.message ?: "Failed to load data")
                }
            )
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            _currentFilter.value = _currentFilter.value.copy(searchQuery = query)
            applyFilter(_currentFilter.value)
        }
    }

    fun applyFilter(filter: MealFilter) {
        viewModelScope.launch {
            _currentFilter.value = filter
            var filtered = _meals.value

            // Apply search query filter
            if (filter.searchQuery.isNotBlank()) {
                filtered = filtered.filter { meal ->
                    meal.strMeal?.contains(filter.searchQuery, ignoreCase = true) == true ||
                            meal.strCategory?.contains(
                                filter.searchQuery,
                                ignoreCase = true
                            ) == true ||
                            meal.strArea?.contains(filter.searchQuery, ignoreCase = true) == true
                }
            }

            // Apply category filter
            if (filter.categories.isNotEmpty()) {
                filtered = filtered.filter { meal ->
                    meal.strCategory in filter.categories
                }
            }

            // Apply area filter
            if (filter.areas.isNotEmpty()) {
                filtered = filtered.filter { meal ->
                    meal.strArea in filter.areas
                }
            }

            // Apply sorting
            filtered = when (filter.sortBy) {
                SortOption.NAME_ASC -> filtered.sortedBy { it.strMeal }
                SortOption.NAME_DESC -> filtered.sortedByDescending { it.strMeal }
                SortOption.CATEGORY -> filtered.sortedBy { it.strCategory }
                SortOption.AREA -> filtered.sortedBy { it.strArea }
            }

            _filteredMeals.value = filtered
            _mealState.value = if (filtered.isEmpty()) {
                MealState.Error("No meals found matching your filters")
            } else {
                MealState.Success(filtered)
            }
        }
    }

    fun clearFilters() {
        viewModelScope.launch {
            _currentFilter.value = MealFilter()
            _filteredMeals.value = _meals.value
            _mealState.value = MealState.Success(_meals.value)
        }
    }

    private fun updateFilterOptions(meals: List<MealResponse>) {
        val categories = meals.mapNotNull { it.strCategory }.distinct().sorted()
        val areas = meals.mapNotNull { it.strArea }.distinct().sorted()
        _filterOptions.value = FilterOptions(
            availableCategories = categories,
            availableAreas = areas
        )
    }

    fun findMealById(mealId: String?): MealResponse? {
        return meals.value.find { it.idResponse == mealId }
    }

    private fun filterMealsByTime() {
        viewModelScope.launch {
            _mealsByTime.emit(meals.value.filter { it.asDto().category == getMealCategoryByTime() })
        }
    }

    fun hasActiveFilters(): Boolean {
        val filter = _currentFilter.value
        return filter.searchQuery.isNotBlank() ||
                filter.categories.isNotEmpty() ||
                filter.areas.isNotEmpty() ||
                filter.sortBy != SortOption.NAME_ASC
    }
}

sealed class MealState {
    data object Loading: MealState()
    data class Success(val data : List<MealResponse>): MealState()
    data class Error(val message: String): MealState()
}