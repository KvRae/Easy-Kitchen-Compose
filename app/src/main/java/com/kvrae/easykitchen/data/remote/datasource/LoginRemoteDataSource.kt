package com.kvrae.easykitchen.data.remote.datasource

import com.kvrae.easykitchen.data.remote.dto.ErrorResponse
import com.kvrae.easykitchen.data.remote.dto.LoginRequest
import com.kvrae.easykitchen.data.remote.dto.LoginResponse
import com.kvrae.easykitchen.domain.exceptions.AuthException
import com.kvrae.easykitchen.utils.LOGIN_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

interface LoginRemoteDataSource {
    suspend fun login(request: LoginRequest): LoginResponse
}

class LoginRemoteDataSourceImpl(private val client: HttpClient) : LoginRemoteDataSource {

    override suspend fun login(request: LoginRequest): LoginResponse {
        return try {
            val response = client.post(LOGIN_URL) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            when {
                response.status.isSuccess() -> response.body()
                response.status.value == 400 -> throw mapLoginError(response.body(), 400)
                response.status.value == 401 -> throw mapLoginError(response.body(), 401)
                response.status.value == 500 -> throw mapLoginError(response.body(), 500)
                else -> throw mapLoginError(response.body(), response.status.value)
            }
        } catch (e: AuthException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("LoginDataSource", "Error during login", e)
            throw mapNetworkException(e)
        }
    }

    private fun mapLoginError(errorBody: ErrorResponse, status: Int): AuthException {
        val message = errorBody.error ?: errorBody.message

        return when (status) {
            400 -> AuthException.Login.InvalidCredentials(
                message
                    ?: "Invalid login credentials. Please check your username/email and password."
            )

            401 -> AuthException.Login.InvalidCredentials(
                message ?: "Invalid username or password. Please try again."
            )

            500 -> AuthException.Login.ServerUnreachable()
            else -> AuthException.Login.UnknownError(
                message ?: "Unable to sign in. Please try again."
            )
        }
    }

    private fun mapNetworkException(exception: Exception): AuthException {
        return when {
            exception.message?.contains("timeout", ignoreCase = true) == true ->
                AuthException.Login.ConnectionTimeout()

            exception.message?.contains("Unable to resolve host", ignoreCase = true) == true ->
                AuthException.Login.ServerUnreachable()

            exception.message?.contains("Failed to connect", ignoreCase = true) == true ->
                AuthException.Login.ServerUnreachable()

            exception.message?.contains("Connection refused", ignoreCase = true) == true ->
                AuthException.Login.ServerUnreachable()

            exception.message?.contains("SSLHandshakeException", ignoreCase = true) == true ->
                AuthException.Login.UnknownError("Secure connection failed. Please try again.")

            exception is kotlinx.serialization.SerializationException ->
                AuthException.Login.UnknownError("Invalid response from server. Please try again later.")

            else ->
                AuthException.Login.UnknownError(
                    exception.message
                        ?: "Unable to sign in. Please check your connection and try again."
                )
        }
    }
}

