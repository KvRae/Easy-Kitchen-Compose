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
            Result.success(remoteDataSource.login(request))
        } catch (e: SocketTimeoutException) {
            Result.failure(AuthException.Login.ConnectionTimeout())
        } catch (e: ConnectException) {
            Result.failure(AuthException.Login.ServerUnreachable())
        } catch (e: Exception) {
            Result.failure(AuthException.Login.UnknownError(e.message ?: "Unknown error"))
        }
    }
}