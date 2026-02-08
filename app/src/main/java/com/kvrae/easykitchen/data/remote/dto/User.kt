package com.kvrae.easykitchen.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("_id")
    val id: String? = null,
    @SerialName("username")
    val username: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("password")
    val password: String? = null,
    @SerialName("phone")
    val phone: String? = null
)

@Serializable
data class LoginRequest(
    @SerialName("username")
    val username: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("password")
    val password: String
)

@Serializable
data class LoginResponse(
    @SerialName("message")
    val message: String? = null,
    @SerialName("user")
    val user: User = User(),
    @SerialName("token")
    val token: String = "",
    @SerialName("error")
    val error: String? = null
)

@Serializable
data class RegisterRequest(
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)

@Serializable
data class RegisterResponse(
    @SerialName("user")
    val user: User = User(),
    @SerialName("message")
    val message: String = "",
    @SerialName("error")
    val error: String? = null
)

@Serializable
data class ErrorResponse(
    val error: String? = null,
    val message: String? = null
)
