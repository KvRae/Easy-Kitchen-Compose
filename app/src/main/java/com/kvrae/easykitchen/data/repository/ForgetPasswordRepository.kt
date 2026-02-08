package com.kvrae.easykitchen.data.repository

import com.kvrae.easykitchen.data.remote.datasource.ForgetPasswordRemoteDataSource
import com.kvrae.easykitchen.data.remote.datasource.ResetPasswordRemoteDataSource
import com.kvrae.easykitchen.data.remote.datasource.VerifyOtpRemoteDataSource
import com.kvrae.easykitchen.data.remote.dto.ApiErrorResponse
import com.kvrae.easykitchen.data.remote.dto.ForgetPasswordResponse
import com.kvrae.easykitchen.data.remote.dto.ResetPasswordResponse
import com.kvrae.easykitchen.data.remote.dto.VerifyOtpResponse
import com.kvrae.easykitchen.domain.exceptions.AuthException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

interface ForgetPasswordRepository {
    suspend fun forgetPassword(email: String): Result<ForgetPasswordResponse>
    suspend fun verifyOtp(resetCode: String, token: String): Result<VerifyOtpResponse>
    suspend fun resetPassword(email: String, password: String): Result<ResetPasswordResponse>
}

class ForgetPasswordRepositoryImpl(
    private val forgetPasswordRemoteDataSource: ForgetPasswordRemoteDataSource,
    private val verifyOtpRemoteDataSource: VerifyOtpRemoteDataSource,
    private val resetPasswordRemoteDataSource: ResetPasswordRemoteDataSource
) : ForgetPasswordRepository {

    // Lenient JSON to decode error bodies safely
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun forgetPassword(email: String): Result<ForgetPasswordResponse> {
        return try {
            Result.success(forgetPasswordRemoteDataSource.forgetPassword(email))
        } catch (e: AuthException) {
            Result.failure(e)
        } catch (e: ClientRequestException) { // 4xx
            Result.failure(mapForgetPasswordException(e))
        } catch (e: ServerResponseException) { // 5xx
            Result.failure(AuthException.PasswordRecovery.ServerUnreachable())
        } catch (e: HttpRequestTimeoutException) {
            Result.failure(AuthException.PasswordRecovery.ConnectionTimeout())
        } catch (e: Exception) {
            Result.failure(
                AuthException.PasswordRecovery.NetworkError(
                    e.message ?: "Unknown error occurred"
                )
            )
        }
    }

    override suspend fun verifyOtp(resetCode: String, token: String): Result<VerifyOtpResponse> {
        return try {
            Result.success(verifyOtpRemoteDataSource.verifyOtp(resetCode, token))
        } catch (e: AuthException) {
            Result.failure(e)
        } catch (e: ClientRequestException) {
            Result.failure(mapVerifyOtpException(e))
        } catch (e: ServerResponseException) {
            Result.failure(AuthException.OtpVerification.ServerUnreachable())
        } catch (e: HttpRequestTimeoutException) {
            Result.failure(AuthException.OtpVerification.ConnectionTimeout())
        } catch (e: Exception) {
            Result.failure(
                AuthException.OtpVerification.UnknownError(
                    e.message ?: "Unknown error occurred"
                )
            )
        }
    }

    override suspend fun resetPassword(
        email: String,
        password: String
    ): Result<ResetPasswordResponse> {
        return try {
            Result.success(resetPasswordRemoteDataSource.resetPassword(email, password))
        } catch (e: AuthException) {
            Result.failure(e)
        } catch (e: ClientRequestException) {
            Result.failure(mapResetPasswordException(e))
        } catch (e: ServerResponseException) {
            Result.failure(AuthException.PasswordReset.ServerUnreachable())
        } catch (e: HttpRequestTimeoutException) {
            Result.failure(AuthException.PasswordReset.ConnectionTimeout())
        } catch (e: Exception) {
            Result.failure(
                AuthException.PasswordReset.UnknownError(
                    e.message ?: "Unknown error occurred"
                )
            )
        }
    }

    private suspend fun mapForgetPasswordException(e: ClientRequestException): AuthException {
        val status = e.response.status.value
        val body = runCatching { e.response.bodyAsText() }.getOrNull()
        val apiError =
            body?.let { runCatching { json.decodeFromString<ApiErrorResponse>(it) }.getOrNull() }
        val message = apiError?.error ?: apiError?.message

        return when (status) {
            400 -> AuthException.PasswordRecovery.InvalidEmail(
                message ?: "Invalid email address. Please check and try again."
            )

            404 -> AuthException.PasswordRecovery.UserNotFound(
                message ?: "Email address not found. Please check and try again."
            )

            else -> AuthException.PasswordRecovery.UnknownError(
                message ?: "Unable to send reset code. Please try again."
            )
        }
    }

    private suspend fun mapVerifyOtpException(e: ClientRequestException): AuthException {
        val status = e.response.status.value
        val body = runCatching { e.response.bodyAsText() }.getOrNull()
        val apiError =
            body?.let { runCatching { json.decodeFromString<ApiErrorResponse>(it) }.getOrNull() }
        val message = apiError?.error ?: apiError?.message

        return when (status) {
            400 -> AuthException.OtpVerification.InvalidCode(
                message ?: "Invalid verification code. Please check and try again."
            )

            401, 403 -> AuthException.OtpVerification.ExpiredCode(
                message ?: "Verification code has expired. Please request a new one."
            )

            else -> AuthException.OtpVerification.VerificationFailed(
                message ?: "Unable to verify code. Please try again."
            )
        }
    }

    private suspend fun mapResetPasswordException(e: ClientRequestException): AuthException {
        val status = e.response.status.value
        val body = runCatching { e.response.bodyAsText() }.getOrNull()
        val apiError =
            body?.let { runCatching { json.decodeFromString<ApiErrorResponse>(it) }.getOrNull() }
        val message = apiError?.error ?: apiError?.message

        return when (status) {
            400 -> AuthException.PasswordReset.WeakPassword(
                message ?: "Invalid password. Please use at least 6 characters."
            )

            404 -> AuthException.PasswordReset.AccountNotFound(
                message ?: "Account not found. Please try again."
            )

            else -> AuthException.PasswordReset.UnknownError(
                message ?: "Unable to reset password. Please try again."
            )
        }
    }
}
