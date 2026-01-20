package com.kvrae.easykitchen.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvrae.easykitchen.data.remote.dto.CategoryResponse
import com.kvrae.easykitchen.data.remote.dto.MealResponse
import com.kvrae.easykitchen.domain.model.UserLocation
import com.kvrae.easykitchen.domain.usecases.FilterMealsByAreaUseCase
import com.kvrae.easykitchen.domain.usecases.GetCategoryUseCase
import com.kvrae.easykitchen.domain.usecases.GetMealsUseCase
import com.kvrae.easykitchen.domain.usecases.GetUserLocationUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.min

class HomeViewModel(
    private val mealsUseCase: GetMealsUseCase,
    private val categoryUseCase: GetCategoryUseCase,
    private val getUserLocationUseCase: GetUserLocationUseCase,
    private val filterMealsByAreaUseCase: FilterMealsByAreaUseCase
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState

    private val _userLocation = MutableStateFlow<UserLocation?>(null)
    val userLocation: StateFlow<UserLocation?> = _userLocation

    private val _lastViewedMealId = MutableStateFlow<String?>(null)
    val lastViewedMealId: StateFlow<String?> = _lastViewedMealId

    private var isDataLoaded = false

    init {
        getData()
    }

    fun getData(forceRefresh: Boolean = false) {
        // Skip fetching if data is already loaded and not forcing refresh
        if (isDataLoaded && !forceRefresh) {
            return
        }

        viewModelScope.launch {
            // Only set to Loading if we don't have data already
            if (!isDataLoaded) {
                _homeState.value = HomeState.Loading
            }

            val categoryResult = async { categoryUseCase() }.await()
            val mealResult = async { mealsUseCase() }.await()
            val locationResult = async { getUserLocationUseCase() }.await()

            // Store user location
            locationResult.getOrNull()?.let {
                _userLocation.value = it
            }

            _homeState.value = when {
                categoryResult.isSuccess && mealResult.isSuccess -> {
                    val categories = categoryResult.getOrNull() ?: emptyList()
                    val meals = mealResult.getOrNull() ?: emptyList()
                    isDataLoaded = true
                    HomeState.Success(meals, categories)
                }
                categoryResult.isFailure || mealResult.isFailure -> {
                    // Only show error if we don't already have data to show
                    if (isDataLoaded) _homeState.value else HomeState.Error("Failed to load Data")
                }

                else -> {
                    if (isDataLoaded) _homeState.value else HomeState.Error("Contents Not Available!")
                }
            }
        }
    }

    fun getMealsByLocation(meals: List<MealResponse>): List<MealResponse> {
        val location = _userLocation.value ?: return emptyList()
        val area = location.cuisineArea
        if (area.isBlank() || area.equals("Unknown", ignoreCase = true)) {
            return emptyList()
        }
        return filterMealsByAreaUseCase(meals, area)
    }

    fun onMealViewed(mealId: String?) {
        if (!mealId.isNullOrBlank()) {
            _lastViewedMealId.value = mealId
        }
    }

    fun getFeaturedMeal(meals: List<MealResponse>): MealResponse? {
        val lastId = _lastViewedMealId.value ?: return null
        return meals.firstOrNull { it.idResponse == lastId }
    }

    fun getLocationSection(meals: List<MealResponse>): Pair<String, List<MealResponse>> {
        val location = _userLocation.value
        val area = location?.cuisineArea.orEmpty()
        val areaMeals = getMealsByLocation(meals)

        if (areaMeals.isNotEmpty()) {
            val title = if (area.isNotBlank()) {
                "Based on your Location ($area)"
            } else {
                "Based on your Location"
            }
            return title to areaMeals
        }

        val fallbackMeals = getPopularOrSeasonalMeals(meals)
        return "Popular picks for you" to fallbackMeals
    }

    fun getMealsByTimeOfDay(meals: List<MealResponse>): Pair<String, List<MealResponse>> {
        val timeOfDay = getCurrentTimeOfDay()
        val categories = getCategoriesForTimeOfDay(timeOfDay)

        val filteredMeals = meals.filter { meal ->
            categories.any { category ->
                meal.strCategory.equals(category, ignoreCase = true)
            }
        }.take(10)

        val title = "Ideas for $timeOfDay"
        return title to filteredMeals
    }

    private fun getCurrentTimeOfDay(): String {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when {
            hour in 5..10 -> "Breakfast"
            hour in 11..15 -> "Lunch"
            hour in 16..20 -> "Dinner"
            else -> "Late Night"
        }
    }

    private fun getCategoriesForTimeOfDay(timeOfDay: String): List<String> {
        return when (timeOfDay) {
            "Breakfast" -> listOf("Breakfast", "Dessert", "Starter")
            "Lunch" -> listOf("Pasta", "Chicken", "Beef", "Lamb", "Pork")
            "Dinner" -> listOf("Miscellaneous", "Seafood", "Vegetarian", "Vegan", "Side")
            "Late Night" -> listOf("Dessert", "Starter", "Side")
            else -> listOf("Breakfast")
        }
    }

    private fun getPopularOrSeasonalMeals(meals: List<MealResponse>): List<MealResponse> {
        if (meals.isEmpty()) return emptyList()
        val mealsByCategory = meals.groupBy { it.strCategory.orEmpty() }
        val sortedCategories = mealsByCategory.entries
            .sortedByDescending { it.value.size }
            .map { it.key }
        val selected = mutableListOf<MealResponse>()
        for (category in sortedCategories) {
            if (selected.size >= 10) break
            selected += mealsByCategory[category]
                .orEmpty()
                .take(maxOf(1, min(3, 10 - selected.size)))
        }
        return selected.take(min(10, selected.size))
    }
}

sealed class HomeState{
    data object Loading : HomeState()
    data class Success(
        val meals: List<MealResponse>,
        val categories: List<CategoryResponse>
    ) : HomeState()
    data class Error(val message: String) : HomeState()
}
