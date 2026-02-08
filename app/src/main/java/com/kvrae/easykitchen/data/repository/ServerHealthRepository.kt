package com.kvrae.easykitchen.data.repository

import com.kvrae.easykitchen.data.remote.datasource.ServerHealthRemoteDataSource

/**
 * Repository for server health operations.
 * Follows Clean Architecture principles by abstracting data source details.
 */
interface ServerHealthRepository {
    suspend fun pingServer(): Result<Boolean>
}

class ServerHealthRepositoryImpl(
    private val remoteDataSource: ServerHealthRemoteDataSource
) : ServerHealthRepository {

    override suspend fun pingServer(): Result<Boolean> {
        return try {
            remoteDataSource.pingServer()
        } catch (e: Exception) {
            android.util.Log.d("ServerHealthRepository", "Ping exception: ${e.message}")
            // Return success anyway to not block the splash screen
            Result.success(true)
        }
    }
}
