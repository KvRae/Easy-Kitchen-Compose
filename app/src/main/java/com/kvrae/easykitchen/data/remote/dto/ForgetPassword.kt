package com.kvrae.easykitchen.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForgetPasswordRequest (
    val email: String,
)

@Serializable
data class ForgetPasswordResponse(
    val message: String,
    val email: String? = null,
    val token: String? = null,
)

@Serializable
data class VerifyOtpRequest(
    val resetCode: String,
    val token: String
)

@Serializable
data class VerifyOtpResponse(
    val message: String,
    val verified: Boolean? = null,
)

@Serializable
data class ResetPasswordRequest(
    val email: String,
    val password: String,
)

@Serializable
data class ResetPasswordResponse(
    val message: String,
    val email: String? = null,
)

// Unified error payload returned by the auth/forgot-password endpoints
@Serializable
data class ApiErrorResponse(
    val message: String? = null,
    val error: String? = null,
)
