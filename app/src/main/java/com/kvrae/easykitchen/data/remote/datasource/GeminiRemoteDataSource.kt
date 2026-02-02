package com.kvrae.easykitchen.data.remote.datasource

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.kvrae.easykitchen.utils.CHAT_API_KEY

interface GeminiRemoteDataSource {
    suspend fun sendMessage(prompt: String): String
}

class GeminiRemoteDataSourceImpl : GeminiRemoteDataSource {
    private val TAG = "GeminiRemoteDataSource"
    private val modelName = "gemini-2.5-flash-lite"

    init {
        // Validate API key at initialization
        if (CHAT_API_KEY.isEmpty() || CHAT_API_KEY.isBlank()) {
            Log.e(TAG, "CHAT_API_KEY is empty or blank")
        } else if (CHAT_API_KEY.length < 20) {
            Log.w(TAG, "CHAT_API_KEY seems invalid (too short)")
        } else {
            Log.d(TAG, "CHAT_API_KEY is configured (length: ${CHAT_API_KEY.length})")
        }
    }

    private val generativeModel by lazy {
        try {
            if (CHAT_API_KEY.isEmpty() || CHAT_API_KEY.isBlank() || CHAT_API_KEY == "YOUR_API_KEY_HERE") {
                Log.e(TAG, "Invalid or missing API key")
                throw IllegalStateException("AI Chef service is not configured. Please contact support.")
            }

            GenerativeModel(
                modelName = modelName,
                apiKey = CHAT_API_KEY,
                systemInstruction = content {
                    text("You are a professional, world-class executive chef. Your goal is to help users in the kitchen by providing expert culinary advice, detailed recipes, cooking techniques, and encouragement. Your tone should be helpful, professional, and passionate about food.")
                }
            ).also {
                Log.d(TAG, "Gemini Model initialized successfully")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize Gemini Model", e)
            throw IllegalStateException("AI Chef service is temporarily unavailable. Please try again later.")
        }
    }

    override suspend fun sendMessage(prompt: String): String {
        return try {
            Log.d(TAG, "Sending message to Gemini API")

            if (CHAT_API_KEY.isEmpty() || CHAT_API_KEY == "YOUR_API_KEY_HERE") {
                throw IllegalStateException("AI Chef service is not configured properly. Please contact support.")
            }

            val response = generativeModel.generateContent(prompt)
            val responseText = response.text

            if (responseText.isNullOrBlank()) {
                Log.w(TAG, "Received empty response from Gemini")
                throw IllegalStateException("Chef couldn't generate a response. Please try rephrasing your question.")
            }

            Log.d(TAG, "Successfully received response from Gemini")
            responseText

        } catch (e: IllegalStateException) {
            Log.e(TAG, "Configuration error: ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message to Gemini", e)
            val errorMessage = when {
                e.message?.contains("API key", ignoreCase = true) == true ->
                    "AI Chef service authentication failed. Please try again later."

                e.message?.contains("quota", ignoreCase = true) == true ||
                        e.message?.contains("rate limit", ignoreCase = true) == true ->
                    "AI Chef is busy right now. Please wait a moment and try again."

                e.message?.contains("timeout", ignoreCase = true) == true ->
                    "Connection timed out. Please check your internet and try again."

                e.message?.contains("network", ignoreCase = true) == true ||
                        e.message?.contains("connection", ignoreCase = true) == true ->
                    "Unable to reach AI Chef. Please check your internet connection."

                e.message?.contains("safety", ignoreCase = true) == true ||
                        e.message?.contains("blocked", ignoreCase = true) == true ->
                    "Your message couldn't be processed. Please try a different request."

                else -> "Chef is taking a break. Please try again in a moment."
            }
            throw Exception(errorMessage)
        }
    }
}
