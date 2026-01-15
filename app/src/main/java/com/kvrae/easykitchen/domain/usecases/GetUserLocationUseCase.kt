package com.kvrae.easykitchen.domain.usecases

import com.kvrae.easykitchen.data.repository.LocationRepository
import com.kvrae.easykitchen.domain.model.UserLocation

/**
 * Use case for getting user's location and mapping it to cuisine area
 */
class GetUserLocationUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Result<UserLocation> {
        return locationRepository.getUserLocation()
    }

    fun hasLocationPermission(): Boolean {
        return locationRepository.hasLocationPermission()
    }
}

