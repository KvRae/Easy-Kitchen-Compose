package com.kvrae.easykitchen.data.remote.datasource

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.kvrae.easykitchen.utils.CHAT_API_KEY

interface GeminiRemoteDataSource {
    suspend fun sendMessage(prompt: String): String
}

class GeminiRemoteDataSourceImpl : GeminiRemoteDataSource {
    private val modelName = "gemini-2.5-flash-lite"
    private val generativeModel = GenerativeModel(
        modelName = modelName,
        apiKey = CHAT_API_KEY,
        systemInstruction = content {
            text("You are a professional, world-class executive chef. Your goal is to help users in the kitchen by providing expert culinary advice, detailed recipes, cooking techniques, and encouragement. Your tone should be helpful, professional, and passionate about food.")
        }
    )

    override suspend fun sendMessage(prompt: String): String {
        val response = generativeModel.generateContent(prompt)
        return response.text ?: "No response from Gemini"
    }
}
