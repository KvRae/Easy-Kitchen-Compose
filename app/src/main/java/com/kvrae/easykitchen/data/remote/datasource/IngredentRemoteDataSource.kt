package com.kvrae.easykitchen.data.remote.datasource

import com.kvrae.easykitchen.data.remote.dto.IngredientResponse
import com.kvrae.easykitchen.utils.INGREDIENTS_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface IngredientRemoteDataSource {
    suspend fun getIngredients(): List<IngredientResponse>

}

class IngredientRemoteDataSourceImpl(private val client: HttpClient) : IngredientRemoteDataSource {
    override suspend fun getIngredients(): List<IngredientResponse> {
        return client.get(INGREDIENTS_URL).body()

    }
}