package com.kvrae.easykitchen.data.repository

import com.kvrae.easykitchen.data.remote.datasource.LoginRemoteDataSource
import com.kvrae.easykitchen.data.remote.dto.LoginRequest
import com.kvrae.easykitchen.data.remote.dto.LoginResponse
import com.kvrae.easykitchen.domain.exceptions.AuthException

interface LoginRepository {
    suspend fun login(request: LoginRequest): Result<LoginResponse>
}

class LoginRepositoryImpl(private val remoteDataSource: LoginRemoteDataSource) : LoginRepository {
    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = remoteDataSource.login(request)

            // Validate that we have a valid token and user
            if (response.token.isEmpty() || response.user.username.isNullOrEmpty()) {
                android.util.Log.d(
                    "LoginRepository",
                    "Invalid response: missing token or user data"
                )
                return Result.failure(
                    AuthException.Login.InvalidCredentials(
                        errorMessage = "Invalid credentials"
                    )
                )
            }

            android.util.Log.d(
                "LoginRepository",
                "Login successful for user: ${response.user.username}"
            )
            Result.success(response)
        } catch (e: AuthException) {
            android.util.Log.d("LoginRepository", "Authentication error: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            android.util.Log.d("LoginRepository", "Unexpected error: ${e.message}", e)
            Result.failure(
                AuthException.Login.UnknownError(
                    e.message ?: "Unknown error occurred"
                )
            )
        }
    }
}

