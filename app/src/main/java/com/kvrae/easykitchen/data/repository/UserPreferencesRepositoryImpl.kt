package com.kvrae.easykitchen.data.repository

import com.kvrae.easykitchen.domain.repository.UserPreferencesRepository
import com.kvrae.easykitchen.utils.UserPreferencesManager

/**
 * Implementation of UserPreferencesRepository
 * Bridges the domain layer with the data layer using UserPreferencesManager
 */
class UserPreferencesRepositoryImpl(
    private val userPreferencesManager: UserPreferencesManager
) : UserPreferencesRepository {

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        userPreferencesManager.setOnboardingCompleted(completed)
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        return userPreferencesManager.isOnboardingCompleted()
    }
}
