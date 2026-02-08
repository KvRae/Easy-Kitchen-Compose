package com.kvrae.easykitchen.domain.repository

import com.kvrae.easykitchen.domain.model.MessageLimitStatus

interface MessageLimitRepository {
    suspend fun getStatus(maxPerDay: Int): MessageLimitStatus
    suspend fun tryConsume(maxPerDay: Int): MessageLimitStatus
}
