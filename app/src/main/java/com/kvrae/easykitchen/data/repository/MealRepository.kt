package com.kvrae.easykitchen.data.repository

import com.kvrae.easykitchen.data.remote.datasource.MealRemoteDataSource
import com.kvrae.easykitchen.data.remote.dto.MealResponse

interface MealRepository {
    suspend fun getMeals(): Result<List<MealResponse>>
}

class MealRepositoryImpl(
    private val remoteDataSource: MealRemoteDataSource,
    // private val localDataSource: MealDao // COMMENTED TO PREVENT CRASH
) : MealRepository {

    override suspend fun getMeals(): Result<List<MealResponse>> {
        return try {
            // 1. Try fetching from remote
            val remoteMeals = remoteDataSource.getMeals()

            // 2. Sync with local DB (Clear and Insert) - COMMENTED TO PREVENT CRASH
            /*
            localDataSource.deleteAllMeals()
            localDataSource.insertMeals(remoteMeals.map { it.toEntity() })
            */

            Result.success(remoteMeals)
        } catch (e: Exception) {
            // 3. Fallback to local DB on failure - COMMENTED TO PREVENT CRASH
            /*
            val localMeals = localDataSource.getAllMeals()
            if (localMeals.isNotEmpty()) {
                Result.success(localMeals.map { it.toResponse() })
            } else {
                Result.failure(e)
            }
            */
            Result.failure(e)
        }
    }
}
