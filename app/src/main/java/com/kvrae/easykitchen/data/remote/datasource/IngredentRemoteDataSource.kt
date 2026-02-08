package com.kvrae.easykitchen.data.remote.datasource

import com.kvrae.easykitchen.data.remote.dto.IngredientApiResponse
import com.kvrae.easykitchen.data.remote.dto.IngredientErrorResponse
import com.kvrae.easykitchen.data.remote.dto.IngredientResponse
import com.kvrae.easykitchen.utils.INGREDIENTS_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

interface IngredientRemoteDataSource {
    suspend fun getIngredients(): List<IngredientResponse>
}

class IngredientRemoteDataSourceImpl(private val client: HttpClient) : IngredientRemoteDataSource {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getIngredients(): List<IngredientResponse> {
        return try {
            val response = client.get(INGREDIENTS_URL)

            if (!response.status.value.toString().startsWith("2")) {
                throw Exception("Server returned error: ${response.status.value}")
            }

            val bodyText = response.bodyAsText()

            // Try to parse as API response wrapper first
            try {
                val apiResponse = json.decodeFromString<IngredientApiResponse>(bodyText)

                if (apiResponse.error != null) {
                    throw Exception(apiResponse.error)
                }

                if (apiResponse.data != null && apiResponse.data.isNotEmpty()) {
                    apiResponse.data
                } else {
                    throw Exception(apiResponse.message ?: "No ingredients found")
                }
            } catch (_: Exception) {
                // If wrapper parsing fails, try to parse as array directly
                try {
                    json.decodeFromString<List<IngredientResponse>>(bodyText)
                } catch (_: Exception) {
                    // If array parsing fails, try to parse as error object
                    try {
                        val errorResponse = json.decodeFromString<IngredientErrorResponse>(bodyText)
                        throw Exception(
                            errorResponse.message ?: errorResponse.error
                            ?: "Failed to load ingredients"
                        )
                    } catch (_: Exception) {
                        throw Exception("Unable to parse ingredients response. Server may be returning unexpected format.")
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("IngredientDataSource", "Error fetching ingredients", e)
            throw e
        }
    }
}

