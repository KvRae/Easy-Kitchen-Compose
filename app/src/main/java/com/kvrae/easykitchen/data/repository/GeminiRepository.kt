package com.kvrae.easykitchen.data.repository

import android.util.Log
import com.kvrae.easykitchen.data.remote.datasource.GeminiRemoteDataSource

interface GeminiRepository {
    suspend fun sendMessage(prompt: String): Result<String>
}

class GeminiRepositoryImpl(private val remoteDataSource: GeminiRemoteDataSource) : GeminiRepository {
    private val TAG = "GeminiRepository"

    override suspend fun sendMessage(prompt: String): Result<String> {
        return try {
            Log.d(TAG, "Sending message to remote data source")
            val response = remoteDataSource.sendMessage(prompt)
            Log.d(TAG, "Successfully received response")
            Result.success(response)
        } catch (e: Exception) {
            Log.e(TAG, "Error in repository layer: ${e.message}", e)
            Result.failure(e)
        }
    }
}
