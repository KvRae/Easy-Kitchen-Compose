package com.kvrae.easykitchen.domain.usecases

import com.kvrae.easykitchen.data.repository.GeminiRepository

class GeminiChatUseCase(
    private val repository: GeminiRepository
) {
    suspend operator fun invoke(prompt: String): Result<String> {
        return repository.sendMessage(prompt)
    }
}
