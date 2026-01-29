package com.kvrae.easykitchen.data.remote.datasource

import com.kvrae.easykitchen.data.remote.dto.MealApiResponse
import com.kvrae.easykitchen.data.remote.dto.MealErrorResponse
import com.kvrae.easykitchen.data.remote.dto.MealResponse
import com.kvrae.easykitchen.utils.MEALS_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

interface MealRemoteDataSource {
    suspend fun getMeals(): List<MealResponse>
}

class MealsRemoteDataSourceImpl(private val client: HttpClient) : MealRemoteDataSource {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getMeals(): List<MealResponse> {
        return try {
            val response = client.get(MEALS_URL)

            if (!response.status.value.toString().startsWith("2")) {
                throw Exception("Server returned error: ${response.status.value}")
            }

            val bodyText = response.bodyAsText()

            // Try to parse as API response wrapper first
            try {
                val apiResponse = json.decodeFromString<MealApiResponse>(bodyText)

                if (apiResponse.error != null) {
                    throw Exception(apiResponse.error)
                }

                if (apiResponse.data != null && apiResponse.data.isNotEmpty()) {
                    apiResponse.data
                } else {
                    throw Exception(apiResponse.message ?: "No meals found")
                }
            } catch (_: Exception) {
                // If wrapper parsing fails, try to parse as array directly
                try {
                    json.decodeFromString<List<MealResponse>>(bodyText)
                } catch (_: Exception) {
                    // If array parsing fails, try to parse as error object
                    try {
                        val errorResponse = json.decodeFromString<MealErrorResponse>(bodyText)
                        throw Exception(
                            errorResponse.message ?: errorResponse.error ?: "Failed to load meals"
                        )
                    } catch (_: Exception) {
                        throw Exception("Unable to parse meals response. Server may be returning unexpected format.")
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MealsDataSource", "Error fetching meals", e)
            throw e
        }
    }
}

