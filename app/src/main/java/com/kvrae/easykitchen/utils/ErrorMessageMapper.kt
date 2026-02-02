package com.kvrae.easykitchen.utils

/**
 * Maps raw error messages from various sources (Gemini API, network, etc.)
 * to user-friendly error messages
 */
object ErrorMessageMapper {

    fun mapErrorMessage(rawMessage: String?): String {
        if (rawMessage.isNullOrBlank()) {
            return "Unable to complete your request. Please try again."
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
                "Unable to connect. Please check your internet connection."

            lowerMessage.contains("connection refused") ||
                    lowerMessage.contains("server is temporarily unavailable") ->
                "Service is temporarily unavailable. Please try again in a moment."

            lowerMessage.contains("network", ignoreCase = true) ||
                    lowerMessage.contains("connection", ignoreCase = true) ->
                "Network error. Please check your connection and try again."

            // Gemini API / AI Chef specific errors
            lowerMessage.contains("api key") ||
                    lowerMessage.contains("unauthorized") ||
                    lowerMessage.contains("authentication failed") ||
                    lowerMessage.contains("not configured properly") ||
                    lowerMessage.contains("invalid key") ->
                "AI Chef service is temporarily unavailable. Please try again later."

            lowerMessage.contains("quota") ||
                    lowerMessage.contains("rate limit") ||
                    lowerMessage.contains("busy right now") ->
                "AI Chef is busy. Please wait a moment and try again."

            lowerMessage.contains("resource exhausted") ->
                "Service is experiencing high demand. Please try again shortly."

            lowerMessage.contains("invalid argument") ||
                    lowerMessage.contains("malformed") ->
                "Invalid request. Please try rephrasing your message."

            lowerMessage.contains("safety") ||
                    lowerMessage.contains("blocked") ||
                    lowerMessage.contains("couldn't be processed") ->
                "Your message couldn't be processed. Please try a different question."

            lowerMessage.contains("no response") ||
                    lowerMessage.contains("empty response") ||
                    lowerMessage.contains("couldn't generate a response") ->
                "Chef couldn't respond. Please try rephrasing your question."

            lowerMessage.contains("failed to prepare request body") ||
                    lowerMessage.contains("request body") ->
                "Failed to send message. Please try again."

            lowerMessage.contains("taking a break") ->
                "Chef is taking a break. Please try again in a moment."

            lowerMessage.contains("daily limit") ||
                    lowerMessage.contains("limit reached") ->
                rawMessage // Keep limit messages as-is

            // Generic/Unknown errors
            else -> {
                // If message is already user-friendly (doesn't contain technical terms), use it
                val technicalTerms =
                    listOf("exception", "null", "error:", "failed:", "stacktrace", "at ")
                val containsTechnicalTerms = technicalTerms.any { lowerMessage.contains(it) }

                if (!containsTechnicalTerms && rawMessage.length < 100) {
                    rawMessage
                } else {
                    "Unable to complete your request. Please try again."
                }
            }
        }
    }
}
