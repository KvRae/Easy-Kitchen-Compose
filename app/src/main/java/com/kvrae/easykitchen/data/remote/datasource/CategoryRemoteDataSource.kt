package com.kvrae.easykitchen.data.remote.datasource

import com.kvrae.easykitchen.data.remote.dto.CategoryResponse
import com.kvrae.easykitchen.utils.CATEGORIES_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface CategoryRemoteDataSource {
     suspend fun getCategories(): List<CategoryResponse>
}

class CategoryRemoteDataSourceImpl(
    private val client: HttpClient
) : CategoryRemoteDataSource {
    override suspend fun getCategories(): List<CategoryResponse> {
        return client.get(CATEGORIES_URL).body()

    }

}