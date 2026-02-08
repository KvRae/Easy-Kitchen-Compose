package com.kvrae.easykitchen.domain.repository

/**
 * Repository interface for user preferences in the domain layer
 * Follows clean architecture pattern
 */
interface UserPreferencesRepository {
    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun isOnboardingCompleted(): Boolean
}
