package com.kvrae.easykitchen.data.repository

import com.kvrae.easykitchen.data.remote.datasource.LoginRemoteDataSource
import com.kvrae.easykitchen.data.remote.dto.LoginRequest
import com.kvrae.easykitchen.data.remote.dto.LoginResponse
import com.kvrae.easykitchen.domain.exceptions.AuthException
import java.net.ConnectException
import java.net.SocketTimeoutException

interface LoginRepository {
    suspend fun login(request: LoginRequest): Result<LoginResponse>
}

class LoginRepositoryImpl(private val remoteDataSource: LoginRemoteDataSource) : LoginRepository {
    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = remoteDataSource.login(request)


            if (!response.error.isNullOrEmpty()) {
                android.util.Log.d("LoginRepository", "API returned error: ${response.error}")
                return Result.failure(AuthException.Login.InvalidCredentials(errorMessage = response.error))
            }

            // Validate that we have a valid token and user
            if (response.token.isEmpty() || response.user.username.isNullOrEmpty()) {
                android.util.Log.d(
                    "LoginRepository",
                    "Invalid response: missing token or user data"
                )
                return Result.failure(AuthException.Login.InvalidCredentials(errorMessage = "Invalid credentials"))
            }

            android.util.Log.d(
                "LoginRepository",
                "Login successful for user: ${response.user.username}"
            )
            Result.success(response)
        } catch (_: SocketTimeoutException) {
            android.util.Log.d("LoginRepository", "Connection timeout")
            Result.failure(AuthException.Login.ConnectionTimeout())
        } catch (_: ConnectException) {
            android.util.Log.d("LoginRepository", "Server unreachable")
            Result.failure(AuthException.Login.ServerUnreachable())
        } catch (e: Exception) {
            android.util.Log.d("LoginRepository", "Unknown error: ${e.message}", e)
            Result.failure(AuthException.Login.UnknownError(e.message ?: "Unknown error"))
        }
    }
}