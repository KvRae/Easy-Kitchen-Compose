package com.kvrae.easykitchen.data.repository

import com.kvrae.easykitchen.data.remote.datasource.RegisterRemoteDataSource
import com.kvrae.easykitchen.data.remote.dto.RegisterRequest
import com.kvrae.easykitchen.data.remote.dto.RegisterResponse
import com.kvrae.easykitchen.domain.exceptions.AuthException
import io.ktor.network.sockets.SocketTimeoutException
import java.net.ConnectException

interface RegisterRepository {
    suspend fun register(request: RegisterRequest): Result<RegisterResponse>
}

class RegisterRepositoryImpl(private val remoteDataSource: RegisterRemoteDataSource): RegisterRepository {
    override suspend fun register(request: RegisterRequest): Result<RegisterResponse> {
        return try{
            Result.success(remoteDataSource.register(request))
        } catch (e: SocketTimeoutException) {
            Result.failure(AuthException.Register.ConnectionTimeout())
        } catch (e: ConnectException) {
            Result.failure(AuthException.Register.ServerUnreachable())
        } catch (e: Exception) {
            Result.failure(AuthException.Register.UnknownError(e.message ?: "Unknown error"))
        }
    }

}