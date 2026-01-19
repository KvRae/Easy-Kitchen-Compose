package com.kvrae.easykitchen.data.repository

import com.kvrae.easykitchen.data.local.dao.IngredientDao
import com.kvrae.easykitchen.data.mapper.toEntity
import com.kvrae.easykitchen.data.mapper.toResponse
import com.kvrae.easykitchen.data.remote.datasource.IngredientRemoteDataSource
import com.kvrae.easykitchen.data.remote.dto.IngredientResponse

interface IngredientRepository {
    suspend fun getIngredients(): Result<List<IngredientResponse>>

}

class IngredientRepositoryImpl(
    private val remoteDataSource: IngredientRemoteDataSource,
    private val ingredientDao: IngredientDao
) : IngredientRepository {
    override suspend fun getIngredients() : Result<List<IngredientResponse>> {
        return try {
            val remote = remoteDataSource.getIngredients()
            ingredientDao.clear()
            ingredientDao.insertAll(remote.map { it.toEntity() })
            Result.success(remote)
        } catch (e: Exception) {
            val cached = ingredientDao.getAll()
            if (cached.isNotEmpty()) {
                Result.success(cached.map { it.toResponse() })
            } else {
                Result.failure(e)
            }
        }
    }
}
