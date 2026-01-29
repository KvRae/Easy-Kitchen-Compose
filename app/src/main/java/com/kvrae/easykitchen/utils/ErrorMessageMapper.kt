package com.kvrae.easykitchen.utils

/**
 * Maps raw error messages from various sources (Gemini API, network, etc.)
 * to user-friendly error messages
 */
object ErrorMessageMapper {

    fun mapErrorMessage(rawMessage: String?): String {
        if (rawMessage.isNullOrBlank()) {
            return "Something went wrong. Please try again."
        }

        val lowerMessage = rawMessage.lowercase()

        return when {
            // Network/Connection errors
            lowerMessage.contains("timeout") ||
                    lowerMessage.contains("timed out") ->
                "Connection timed out. Please check your internet and try again."

            lowerMessage.contains("unreachable") ||
                    lowerMessage.contains("offline") ||
                    lowerMessage.contains("socket") ->
                "Unable to connect to server. Please check your internet connection."

            lowerMessage.contains("connection refused") ->
                "Server is temporarily unavailable. Please try again later."

            // Gemini API specific errors
            lowerMessage.contains("api key") ||
                    lowerMessage.contains("unauthorized") ||
                    lowerMessage.contains("invalid key") ->
                "Authentication error. Please contact support."

            lowerMessage.contains("quota") ||
                    lowerMessage.contains("rate limit") ->
                "Too many requests. Please wait a moment before trying again."

            lowerMessage.contains("resource exhausted") ->
                "Service temporarily unavailable. Please try again in a few moments."

            lowerMessage.contains("invalid argument") ||
                    lowerMessage.contains("malformed") ->
                "Invalid request. Please try again with a different message."

            lowerMessage.contains("safety") ||
                    lowerMessage.contains("blocked") ->
                "Your message couldn't be processed. Please try a different request."

            lowerMessage.contains("no response") ||
                    lowerMessage.contains("empty") ->
                "Chef couldn't process your request. Please try rephrasing it."

            lowerMessage.contains("failed to prepare request body") ||
                    lowerMessage.contains("request body") ->
                "Failed to send message. Please try again."

            // Generic/Unknown errors
            else -> {
                // If message is too long or contains technical details, make it generic
                if (rawMessage.length > 100) {
                    "An error occurred. Please try again."
                } else {
                    rawMessage
                }
            }
        }
    }
}
