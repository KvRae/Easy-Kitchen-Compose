package com.kvrae.easykitchen.domain.model

/**
 * Domain model representing user's location
 */
data class UserLocation(
    val country: String,
    val cuisineArea: String // Maps to meal area (e.g., "American", "Italian", "Chinese")
)

