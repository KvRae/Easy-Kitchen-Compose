package com.kvrae.easykitchen.presentation.login

import com.kvrae.easykitchen.domain.exceptions.AuthException

/**
 * Maps authentication errors to user-friendly messages for the login screen
 */
object LoginErrorMessageMapper {

    fun mapToUserFriendlyMessage(error: Throwable?): String {
        return when (error) {
            is AuthException.Login.InvalidCredentials -> {
                "Incorrect username or password. Please check your credentials and try again."
            }

            is AuthException.Login.ConnectionTimeout -> {
                "Connection timed out. Please check your internet connection and try again."
            }

            is AuthException.Login.ServerUnreachable -> {
                "Unable to connect to the server. Please check your internet connection and try again."
            }

            is AuthException.Login.UnknownError -> {
                // Check if the message contains specific keywords and make it more friendly
                val message = error.message ?: ""
                when {
                    message.contains("secure connection", ignoreCase = true) ->
                        "Connection error. Please check your internet and try again."

                    message.contains("Invalid response", ignoreCase = true) ->
                        "Service temporarily unavailable. Please try again in a moment."

                    message.contains("Unable to sign in", ignoreCase = true) ->
                        "Unable to sign in. Please check your credentials and try again."

                    else -> message.ifBlank {
                        "Something went wrong. Please try again."
                    }
                }
            }

            else -> {
                // Generic fallback for unexpected errors
                val message = error?.message ?: ""
                when {
                    message.contains("Failed to load data", ignoreCase = true) ->
                        "Unable to connect. Please check your internet connection and try again."

                    message.contains("timeout", ignoreCase = true) ->
                        "Connection timed out. Please try again."

                    message.contains("network", ignoreCase = true) ||
                            message.contains("connection", ignoreCase = true) ->
                        "Network error. Please check your connection and try again."

                    message.isBlank() ->
                        "Unable to sign in. Please try again."

                    else -> message
                }
            }
        }
    }
}
