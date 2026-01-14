package com.kvrae.easykitchen.data.remote.datasource

import com.kvrae.easykitchen.data.remote.dto.MealResponse
import com.kvrae.easykitchen.utils.MEALS_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface MealRemoteDataSource {
    suspend fun getMeals() : List<MealResponse>
}

class MealsRemoteDataSourceImpl(private val client: HttpClient): MealRemoteDataSource{
    override suspend fun getMeals(): List<MealResponse> {
        return client.get(MEALS_URL).body()
    }

}