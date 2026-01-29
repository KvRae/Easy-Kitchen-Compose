package com.kvrae.easykitchen.data.remote.datasource

import com.kvrae.easykitchen.utils.HEALTH_CHECK_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

/**
 * Remote data source for server health checks.
 * Used to wake up the server before user authentication.
 */
interface ServerHealthRemoteDataSource {
    suspend fun pingServer(): Result<Boolean>
}

class ServerHealthRemoteDataSourceImpl(
    private val client: HttpClient
) : ServerHealthRemoteDataSource {

    override suspend fun pingServer(): Result<Boolean> {
        return try {
            // Ping the health check endpoint to wake up the server
            val response: HttpResponse = client.get(HEALTH_CHECK_URL)

            if (response.status.isSuccess()) {
                Result.success(true)
            } else {
                Result.success(false)
            }
        } catch (e: Exception) {
            // Even if it fails, we consider it as server being pinged
            // The actual authentication will handle connection errors properly
            android.util.Log.d(
                "ServerHealthCheck",
                "Ping failed but server may be waking up: ${e.message}"
            )
            Result.success(true)
        }
    }
}
