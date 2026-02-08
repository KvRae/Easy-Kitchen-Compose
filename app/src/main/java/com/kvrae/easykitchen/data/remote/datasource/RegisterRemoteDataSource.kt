package com.kvrae.easykitchen.data.remote.datasource

import com.kvrae.easykitchen.data.remote.dto.ApiErrorResponse
import com.kvrae.easykitchen.data.remote.dto.RegisterRequest
import com.kvrae.easykitchen.data.remote.dto.RegisterResponse
import com.kvrae.easykitchen.domain.exceptions.AuthException
import com.kvrae.easykitchen.utils.REGISTER_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

interface RegisterRemoteDataSource {
    suspend fun register(request: RegisterRequest): RegisterResponse
}

class RegisterRemoteDataSourceImpl(private val client: HttpClient) : RegisterRemoteDataSource {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun register(request: RegisterRequest): RegisterResponse {
        return try {
            val response = client.post(REGISTER_URL) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            when {
                response.status.isSuccess() -> response.body()
                response.status.value == 400 -> throw mapRegisterError(response.bodyAsText(), 400)
                response.status.value == 500 -> throw mapRegisterError(response.bodyAsText(), 500)
                else -> throw mapRegisterError(response.bodyAsText(), response.status.value)
            }
        } catch (e: AuthException) {
            throw e
        } catch (e: Exception) {
            android.util.Log.e("RegisterDataSource", "Error during registration", e)
            throw mapNetworkException(e)
        }
    }

    private fun mapRegisterError(bodyText: String?, status: Int): AuthException {
        val apiError = bodyText?.let {
            runCatching { json.decodeFromString<ApiErrorResponse>(it) }.getOrNull()
        }
        val message = apiError?.error ?: apiError?.message

        return when (status) {
            400 -> {
                // Check if it's a specific error type
                when {
                    message?.contains("already exists", ignoreCase = true) == true ->
                        AuthException.Register.UserAlreadyExists()

                    message?.contains("password", ignoreCase = true) == true ->
                        AuthException.Register.WeakPassword()

                    else ->
                        AuthException.Register.UnknownError(
                            message
                                ?: "Invalid registration details. Please check your information."
                        )
                }
            }

            500 -> AuthException.Register.ServerUnreachable()
            else -> AuthException.Register.UnknownError(
                message ?: "Unable to complete registration. Please try again."
            )
        }
    }

    private fun mapNetworkException(exception: Exception): AuthException {
        return when {
            exception.message?.contains("timeout", ignoreCase = true) == true ->
                AuthException.Register.ConnectionTimeout()

            exception.message?.contains("Unable to resolve host", ignoreCase = true) == true ->
                AuthException.Register.ServerUnreachable()

            exception.message?.contains("Failed to connect", ignoreCase = true) == true ->
                AuthException.Register.ServerUnreachable()

            exception.message?.contains("Connection refused", ignoreCase = true) == true ->
                AuthException.Register.ServerUnreachable()

            exception is kotlinx.serialization.SerializationException ->
                AuthException.Register.UnknownError("Invalid response from server. Please try again later.")

            else ->
                AuthException.Register.UnknownError(
                    exception.message ?: "Unable to complete registration. Please try again."
                )
        }
    }
}

