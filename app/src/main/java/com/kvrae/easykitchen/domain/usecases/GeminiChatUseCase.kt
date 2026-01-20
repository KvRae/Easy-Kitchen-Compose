package com.kvrae.easykitchen.domain.usecases

import com.kvrae.easykitchen.data.repository.GeminiRepository

class GeminiChatUseCase(
    private val repository: GeminiRepository
) {
    suspend operator fun invoke(prompt: String): Result<String> {

        return try {
            repository.sendMessage(prompt)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
