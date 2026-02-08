package com.kvrae.easykitchen.domain.usecases

import com.kvrae.easykitchen.domain.model.MessageLimitStatus
import com.kvrae.easykitchen.domain.repository.MessageLimitRepository

class TryConsumeMessageUseCase(
    private val repository: MessageLimitRepository
) {
    suspend operator fun invoke(maxPerDay: Int): MessageLimitStatus {
        return repository.tryConsume(maxPerDay)
    }
}
