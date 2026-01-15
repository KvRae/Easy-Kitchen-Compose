package com.kvrae.easykitchen.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.kvrae.easykitchen.domain.model.UserLocation
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

interface LocationRepository {
    suspend fun getUserLocation(): Result<UserLocation>
    fun hasLocationPermission(): Boolean
}

class LocationRepositoryImpl(
    private val context: Context
) : LocationRepository {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getUserLocation(): Result<UserLocation> {
        return try {
            if (!hasLocationPermission()) {
                // Without permission, we can't resolve location — return unknown so UI can gracefully fallback
                return Result.success(getDefaultLocation())
            }

            val location = getCurrentLocation()
            if (location != null) {
                val cuisineArea = mapCountryToCuisine(location)
                Result.success(location.copy(cuisineArea = cuisineArea))
            } else {
                // Unknown location
                Result.success(getDefaultLocation())
            }
        } catch (e: Exception) {
            // Any error: return unknown so we don't force an incorrect area
            Result.success(getDefaultLocation())
        }
    }

    override fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private suspend fun getCurrentLocation(): UserLocation? =
        suspendCancellableCoroutine { continuation ->
            try {
                if (!hasLocationPermission()) {
                    continuation.resume(null)
                    return@suspendCancellableCoroutine
                }

                val cancellationTokenSource = CancellationTokenSource()

                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    cancellationTokenSource.token
                ).addOnSuccessListener { location ->
                    if (location != null) {
                        val country = getCountryFromLocation(location.latitude, location.longitude)
                        continuation.resume(
                            UserLocation(
                                country = country,
                                cuisineArea = ""
                            )
                        )
                    } else {
                        continuation.resume(null)
                    }
                }.addOnFailureListener {
                    continuation.resume(null)
                }

                continuation.invokeOnCancellation {
                    cancellationTokenSource.cancel()
                }
            } catch (e: SecurityException) {
                continuation.resume(null)
            }
        }

    private fun getCountryFromLocation(latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                var countryName = "Unknown"
                geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                    if (addresses.isNotEmpty()) {
                        countryName = addresses[0].countryName ?: "Unknown"
                    }
                }
                countryName
            } else {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                addresses?.firstOrNull()?.countryName ?: "Unknown"
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }

    private fun mapCountryToCuisine(location: UserLocation): String {
        // Map countries to meal area cuisine types available in the meal database
        return when (location.country.lowercase()) {
            "united states", "usa", "america" -> "American"
            "united kingdom", "england", "scotland", "wales", "britain" -> "British"
            "canada" -> "Canadian"
            "china" -> "Chinese"
            "croatia" -> "Croatian"
            "netherlands", "holland" -> "Dutch"
            "egypt" -> "Egyptian"
            "philippines" -> "Filipino"
            "france" -> "French"
            "greece" -> "Greek"
            "india" -> "Indian"
            "ireland" -> "Irish"
            "italy" -> "Italian"
            "jamaica" -> "Jamaican"
            "japan" -> "Japanese"
            "kenya" -> "Kenyan"
            "malaysia" -> "Malaysian"
            "mexico" -> "Mexican"
            "morocco" -> "Moroccan"
            "poland" -> "Polish"
            "portugal" -> "Portuguese"
            "russia" -> "Russian"
            "spain" -> "Spanish"
            "thailand" -> "Thai"
            "tunisia" -> "Tunisian"
            "turkey" -> "Turkish"
            "ukraine" -> "Ukrainian"
            "vietnam" -> "Vietnamese"
            else -> "Unknown" // Do not force American; let UI decide fallback
        }
    }

    private fun getDefaultLocation(): UserLocation {
        // Represent unknown location; UI will avoid misleading filters
        return UserLocation(
            country = "Unknown",
            cuisineArea = "Unknown"
        )
    }
}

