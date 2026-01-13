package com.kvrae.easykitchen.data.repository

import com.kvrae.easykitchen.data.remote.datasource.GeminiRemoteDataSource

interface GeminiRepository {
    suspend fun sendMessage(prompt: String): Result<String>
}

class GeminiRepositoryImpl(private val remoteDataSource: GeminiRemoteDataSource) : GeminiRepository {
    override suspend fun sendMessage(prompt: String): Result<String> {
        return try {
            Result.success(remoteDataSource.sendMessage(prompt))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
