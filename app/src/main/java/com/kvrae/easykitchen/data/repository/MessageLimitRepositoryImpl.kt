package com.kvrae.easykitchen.data.repository

import com.kvrae.easykitchen.data.local.datasource.MessageLimitStorage
import com.kvrae.easykitchen.domain.model.MessageLimitStatus
import com.kvrae.easykitchen.domain.repository.MessageLimitRepository
import com.kvrae.easykitchen.domain.util.DateProvider

class MessageLimitRepositoryImpl(
    private val localDataSource: MessageLimitStorage,
    private val dateProvider: DateProvider
) : MessageLimitRepository {

    override suspend fun getStatus(maxPerDay: Int): MessageLimitStatus {
        resetIfNeeded()
        val count = localDataSource.getCount()
        val remaining = (maxPerDay - count).coerceAtLeast(0)
        return if (remaining > 0) {
            MessageLimitStatus.Allowed(remaining = remaining, maxPerDay = maxPerDay)
        } else {
            MessageLimitStatus.LimitReached(maxPerDay = maxPerDay)
        }
    }

    override suspend fun tryConsume(maxPerDay: Int): MessageLimitStatus {
        resetIfNeeded()
        val count = localDataSource.getCount()
        return if (count < maxPerDay) {
            localDataSource.setCount(count + 1)
            val remaining = (maxPerDay - (count + 1)).coerceAtLeast(0)
            MessageLimitStatus.Allowed(remaining = remaining, maxPerDay = maxPerDay)
        } else {
            MessageLimitStatus.LimitReached(maxPerDay = maxPerDay)
        }
    }

    private suspend fun resetIfNeeded() {
        val todayKey = dateProvider.todayKey()
        val storedDate = localDataSource.getDateKey()
        if (storedDate != todayKey) {
            localDataSource.setDateKey(todayKey)
            localDataSource.setCount(0)
        }
    }
}
