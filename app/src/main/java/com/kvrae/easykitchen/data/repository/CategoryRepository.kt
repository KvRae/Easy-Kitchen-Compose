package com.kvrae.easykitchen.data.repository

import com.kvrae.easykitchen.data.local.dao.CategoryDao
import com.kvrae.easykitchen.data.mapper.toEntity
import com.kvrae.easykitchen.data.mapper.toResponse
import com.kvrae.easykitchen.data.remote.datasource.CategoryRemoteDataSource
import com.kvrae.easykitchen.data.remote.dto.CategoryResponse

interface CategoryRepository {
    suspend fun getCategories(): Result<List<CategoryResponse>>

}

class CategoryRepositoryImpl(
    private val remoteDataSource: CategoryRemoteDataSource,
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override suspend fun getCategories(): Result<List<CategoryResponse>> {
        return try {
            val remote = remoteDataSource.getCategories()
            categoryDao.clear()
            categoryDao.insertAll(remote.map { it.toEntity() })
            Result.success(remote)
        } catch (e: Exception) {
            val cached = categoryDao.getAll()
            if (cached.isNotEmpty()) {
                Result.success(cached.map { it.toResponse() })
            } else {
                Result.failure(e)
            }
        }
    }
}