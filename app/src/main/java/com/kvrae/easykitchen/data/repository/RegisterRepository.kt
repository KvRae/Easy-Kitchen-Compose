package com.kvrae.easykitchen.data.repository

import com.kvrae.easykitchen.data.remote.datasource.RegisterRemoteDataSource
import com.kvrae.easykitchen.data.remote.dto.RegisterRequest
import com.kvrae.easykitchen.data.remote.dto.RegisterResponse
import com.kvrae.easykitchen.domain.exceptions.AuthException

interface RegisterRepository {
    suspend fun register(request: RegisterRequest): Result<RegisterResponse>
}

class RegisterRepositoryImpl(private val remoteDataSource: RegisterRemoteDataSource) :
    RegisterRepository {
    override suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = remoteDataSource.register(request)

            // Basic sanity check: username and email should be present
            if (response.user.username.isNullOrBlank() || response.user.email.isNullOrBlank()) {
                return Result.failure(
                    AuthException.Register.UnknownError("Invalid server response")
                )
            }

            android.util.Log.d(
                "RegisterRepository",
                "Registration successful for user: ${response.user.username}"
            )
            Result.success(response)
        } catch (e: AuthException) {
            android.util.Log.d("RegisterRepository", "Authentication error: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            android.util.Log.d("RegisterRepository", "Unexpected error: ${e.message}", e)
            Result.failure(
                AuthException.Register.UnknownError(
                    e.message ?: "Unknown error occurred"
                )
            )
        }
    }
}

