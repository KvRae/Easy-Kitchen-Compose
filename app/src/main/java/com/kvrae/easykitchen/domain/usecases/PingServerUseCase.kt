package com.kvrae.easykitchen.domain.usecases

import com.kvrae.easykitchen.data.repository.ServerHealthRepository

/**
 * Use case for pinging the server to wake it up from inactivity.
 * This is particularly useful for backends hosted on services that
 * spin down with inactivity (like Render free tier, Heroku, etc.)
 */
class PingServerUseCase(
    private val repository: ServerHealthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return repository.pingServer()
    }
}
