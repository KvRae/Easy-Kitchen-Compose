package com.kvrae.easykitchen.presentation.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvrae.easykitchen.data.local.entity.SavedMeal
import com.kvrae.easykitchen.domain.usecases.ClearSavedMealsUseCase
import com.kvrae.easykitchen.domain.usecases.DeleteSavedMealUseCase
import com.kvrae.easykitchen.domain.usecases.GetSavedMealsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SavedMealsViewModel(
    private val getSavedMealsUseCase: GetSavedMealsUseCase,
    private val deleteSavedMealUseCase: DeleteSavedMealUseCase,
    private val clearSavedMealsUseCase: ClearSavedMealsUseCase
) : ViewModel() {

    private val _savedMeals = MutableStateFlow<List<SavedMeal>>(emptyList())
    val savedMeals: StateFlow<List<SavedMeal>> = _savedMeals

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _savedMeals.value = getSavedMealsUseCase()
        }
    }

    fun remove(id: String) {
        viewModelScope.launch {
            deleteSavedMealUseCase(id)
            refresh()
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            clearSavedMealsUseCase()
            refresh()
        }
    }
}
