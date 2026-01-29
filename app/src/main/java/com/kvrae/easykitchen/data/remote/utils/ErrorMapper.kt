package com.kvrae.easykitchen.data.remote.utils

import com.kvrae.easykitchen.data.remote.dto.ApiErrorResponse
import com.kvrae.easykitchen.domain.exceptions.AuthException
import kotlinx.serialization.json.Json

/**
 * Utility object for mapping HTTP errors and exceptions to domain exceptions.
 * Follows Clean Architecture by centralizing error handling logic.
 */
object ErrorMapper {
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Maps network/connectivity exceptions to appropriate AuthException
     */
    fun mapNetworkException(exception: Exception, context: ErrorContext): AuthException {
        val message = exception.message ?: "Unknown error occurred"

        return when {
            message.contains("timeout", ignoreCase = true) -> {
                when (context) {
                    ErrorContext.FORGET_PASSWORD -> AuthException.PasswordRecovery.ConnectionTimeout()
                    ErrorContext.VERIFY_OTP -> AuthException.OtpVerification.ConnectionTimeout()
                    ErrorContext.RESET_PASSWORD -> AuthException.PasswordReset.ConnectionTimeout()
                }
            }

            message.contains("Unable to resolve host", ignoreCase = true) ||
                    message.contains("No internet", ignoreCase = true) -> {
                when (context) {
                    ErrorContext.FORGET_PASSWORD -> AuthException.PasswordRecovery.NetworkError("No internet connection. Please check your network settings.")
                    ErrorContext.VERIFY_OTP -> AuthException.OtpVerification.ConnectionTimeout()
                    ErrorContext.RESET_PASSWORD -> AuthException.PasswordReset.ConnectionTimeout()
                }
            }

            message.contains("Failed to connect", ignoreCase = true) ||
                    message.contains("Connection refused", ignoreCase = true) -> {
                when (context) {
                    ErrorContext.FORGET_PASSWORD -> AuthException.PasswordRecovery.ServerUnreachable()
                    ErrorContext.VERIFY_OTP -> AuthException.OtpVerification.ServerUnreachable()
                    ErrorContext.RESET_PASSWORD -> AuthException.PasswordReset.ServerUnreachable()
                }
            }

            exception is kotlinx.serialization.SerializationException -> {
                when (context) {
                    ErrorContext.FORGET_PASSWORD -> AuthException.PasswordRecovery.UnknownError("Invalid response from server. Please try again later.")
                    ErrorContext.VERIFY_OTP -> AuthException.OtpVerification.UnknownError("Invalid response from server. Please try again later.")
                    ErrorContext.RESET_PASSWORD -> AuthException.PasswordReset.UnknownError("Invalid response from server. Please try again later.")
                }
            }

            else -> {
                when (context) {
                    ErrorContext.FORGET_PASSWORD -> AuthException.PasswordRecovery.UnknownError(
                        message
                    )

                    ErrorContext.VERIFY_OTP -> AuthException.OtpVerification.UnknownError(message)
                    ErrorContext.RESET_PASSWORD -> AuthException.PasswordReset.UnknownError(message)
                }
            }
        }
    }

    /**
     * Maps HTTP status codes and API error responses to domain exceptions
     */
    fun mapApiException(rawBody: String?, status: Int, context: ErrorContext): AuthException {
        val apiError = runCatching {
            json.decodeFromString<ApiErrorResponse>(rawBody ?: "")
        }.getOrNull()

        val errorMessage = apiError?.error ?: apiError?.message

        return when (context) {
            ErrorContext.FORGET_PASSWORD -> mapForgetPasswordError(status, errorMessage)
            ErrorContext.VERIFY_OTP -> mapVerifyOtpError(status, errorMessage)
            ErrorContext.RESET_PASSWORD -> mapResetPasswordError(status, errorMessage)
        }
    }

    private fun mapForgetPasswordError(status: Int, serverMessage: String?): AuthException {
        return when (status) {
            400 -> AuthException.PasswordRecovery.InvalidEmail(
                serverMessage ?: "Invalid email address. Please check and try again."
            )

            404 -> AuthException.PasswordRecovery.UserNotFound(
                serverMessage ?: "Email address not found. Please check and try again."
            )

            500 -> AuthException.PasswordRecovery.UnknownError(
                serverMessage ?: "Server is temporarily unavailable. Please try again later."
            )

            else -> AuthException.PasswordRecovery.UnknownError(
                serverMessage ?: "Unable to send reset code. Please try again."
            )
        }
    }

    private fun mapVerifyOtpError(status: Int, serverMessage: String?): AuthException {
        return when (status) {
            400 -> AuthException.OtpVerification.InvalidCode(
                serverMessage ?: "Invalid verification code. Please check and try again."
            )

            401 -> AuthException.OtpVerification.ExpiredCode(
                serverMessage ?: "Verification code has expired. Please request a new one."
            )

            403 -> AuthException.OtpVerification.InvalidCode(
                serverMessage ?: "Invalid verification code. Please check and try again."
            )

            500 -> AuthException.OtpVerification.UnknownError(
                serverMessage ?: "Server is temporarily unavailable. Please try again later."
            )

            else -> AuthException.OtpVerification.VerificationFailed(
                serverMessage ?: "Unable to verify code. Please try again."
            )
        }
    }

    private fun mapResetPasswordError(status: Int, serverMessage: String?): AuthException {
        return when (status) {
            400 -> AuthException.PasswordReset.WeakPassword(
                serverMessage ?: "Invalid password. Please use at least 6 characters."
            )

            404 -> AuthException.PasswordReset.AccountNotFound(
                serverMessage ?: "Account not found. Please try again."
            )

            500 -> AuthException.PasswordReset.UnknownError(
                serverMessage ?: "Server is temporarily unavailable. Please try again later."
            )

            else -> AuthException.PasswordReset.UnknownError(
                serverMessage ?: "Unable to reset password. Please try again."
            )
        }
    }

    enum class ErrorContext {
        FORGET_PASSWORD,
        VERIFY_OTP,
        RESET_PASSWORD
    }
}
