package com.kvrae.easykitchen.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_preferences")

class UserPreferencesManager(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USERNAME = stringPreferencesKey("username")
        private val EMAIL = stringPreferencesKey("email")
        private val MESSAGE_LIMIT_COUNT = intPreferencesKey("message_limit_count")
        private val MESSAGE_LIMIT_DATE = stringPreferencesKey("message_limit_date")
        private val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
    }

    // Save login state
    suspend fun saveLoginState(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }

    // Read login state
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    // Save username
    suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = username
        }
    }

    // Read username
    val username: Flow<String> = dataStore.data.map { preferences ->
        preferences[USERNAME] ?: ""
    }

    // Save email
    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL] = email
        }
    }

    // Read email
    val email: Flow<String> = dataStore.data.map { preferences ->
        preferences[EMAIL] ?: ""
    }

    // Clear all user data (logout)
    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun getMessageLimitCount(): Int {
        return dataStore.data.first()[MESSAGE_LIMIT_COUNT] ?: 0
    }

    suspend fun setMessageLimitCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[MESSAGE_LIMIT_COUNT] = count
        }
    }

    suspend fun getMessageLimitDate(): String {
        return dataStore.data.first()[MESSAGE_LIMIT_DATE] ?: ""
    }

    suspend fun setMessageLimitDate(date: String) {
        dataStore.edit { preferences ->
            preferences[MESSAGE_LIMIT_DATE] = date
        }
    }

    suspend fun clearMessageLimit() {
        dataStore.edit { preferences ->
            preferences.remove(MESSAGE_LIMIT_COUNT)
            preferences.remove(MESSAGE_LIMIT_DATE)
        }
    }

    // Onboarding completion tracking
    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun isOnboardingCompleted(): Boolean {
        return dataStore.data.first()[IS_ONBOARDING_COMPLETED] ?: false
    }
}
