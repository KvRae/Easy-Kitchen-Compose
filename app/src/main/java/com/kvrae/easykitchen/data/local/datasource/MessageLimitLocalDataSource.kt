package com.kvrae.easykitchen.data.local.datasource

import com.kvrae.easykitchen.utils.UserPreferencesManager

interface MessageLimitStorage {
    suspend fun getCount(): Int
    suspend fun setCount(count: Int)
    suspend fun getDateKey(): String
    suspend fun setDateKey(dateKey: String)
    suspend fun clear()
}

class MessageLimitLocalDataSource(
    private val userPreferencesManager: UserPreferencesManager
) : MessageLimitStorage {
    override suspend fun getCount(): Int = userPreferencesManager.getMessageLimitCount()

    override suspend fun setCount(count: Int) = userPreferencesManager.setMessageLimitCount(count)

    override suspend fun getDateKey(): String = userPreferencesManager.getMessageLimitDate()

    override suspend fun setDateKey(dateKey: String) =
        userPreferencesManager.setMessageLimitDate(dateKey)

    override suspend fun clear() = userPreferencesManager.clearMessageLimit()
}
