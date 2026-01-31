package com.kvrae.easykitchen.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvrae.easykitchen.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing onboarding screen state and user preferences
 * Follows clean architecture pattern with dependency injection
 */
class OnboardingViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _onboardingCompleted = MutableStateFlow(false)
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted

    /**
     * Mark onboarding as completed and save to user preferences
     */
    fun completeOnboarding() {
        viewModelScope.launch {
            try {
                userPreferencesRepository.setOnboardingCompleted(true)
                _onboardingCompleted.value = true
            } catch (e: Exception) {
                // Log error if needed
                e.printStackTrace()
            }
        }
    }

    /**
     * Check if onboarding has been completed
     */
    fun isOnboardingCompleted() {
        viewModelScope.launch {
            try {
                val completed = userPreferencesRepository.isOnboardingCompleted()
                _onboardingCompleted.value = completed
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
