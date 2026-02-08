package com.kvrae.easykitchen.data.remote.datasource

import com.kvrae.easykitchen.data.remote.dto.CategoryApiResponse
import com.kvrae.easykitchen.data.remote.dto.CategoryErrorResponse
import com.kvrae.easykitchen.data.remote.dto.CategoryResponse
import com.kvrae.easykitchen.utils.CATEGORIES_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

interface CategoryRemoteDataSource {
    suspend fun getCategories(): List<CategoryResponse>
}


class CategoryRemoteDataSourceImpl(
    private val client: HttpClient
) : CategoryRemoteDataSource {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override suspend fun getCategories(): List<CategoryResponse> {
        return try {
            val response: HttpResponse = client.get(CATEGORIES_URL)

            if (!response.status.value.toString().startsWith("2")) {
                throw Exception("Server returned error: ${response.status.value}")
            }

            val bodyText = response.bodyAsText()

            // Try to parse as API response wrapper first
            try {
                val apiResponse = json.decodeFromString<CategoryApiResponse>(bodyText)
                if (apiResponse.data != null && apiResponse.data.isNotEmpty()) {
                    apiResponse.data
                } else {
                    throw Exception(apiResponse.message ?: "No categories found")
                }
            } catch (_: Exception) {
                // If wrapper parsing fails, try to parse as array directly
                try {
                    json.decodeFromString<List<CategoryResponse>>(bodyText)
                } catch (_: Exception) {
                    // If array parsing fails, try to parse as error object
                    try {
                        val errorResponse = json.decodeFromString<CategoryErrorResponse>(bodyText)
                        throw Exception(
                            errorResponse.message ?: errorResponse.error
                            ?: "Failed to load categories"
                        )
                    } catch (_: Exception) {
                        throw Exception("Unable to parse categories response. Server may be returning unexpected format.")
                    }
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("CategoryDataSource", "Error fetching categories", e)
            throw e
        }
    }
}