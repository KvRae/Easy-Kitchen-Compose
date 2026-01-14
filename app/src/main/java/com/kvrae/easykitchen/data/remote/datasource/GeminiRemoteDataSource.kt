package com.kvrae.easykitchen.data.remote.datasource

import com.google.ai.client.generativeai.GenerativeModel
import com.kvrae.easykitchen.utils.CHAT_API_KEY

interface GeminiRemoteDataSource {
    suspend fun sendMessage(prompt: String): String
}

class GeminiRemoteDataSourceImpl : GeminiRemoteDataSource {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = CHAT_API_KEY
    )

    override suspend fun sendMessage(prompt: String): String {
        val response = generativeModel.generateContent(prompt)
        return response.text ?: "No response from Gemini"
    }
}
