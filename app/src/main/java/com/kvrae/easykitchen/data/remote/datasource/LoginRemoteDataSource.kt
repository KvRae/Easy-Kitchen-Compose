package com.kvrae.easykitchen.data.remote.datasource

import com.kvrae.easykitchen.data.remote.dto.LoginRequest
import com.kvrae.easykitchen.data.remote.dto.LoginResponse
import com.kvrae.easykitchen.utils.LOGIN_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface LoginRemoteDataSource {
    suspend fun login(request: LoginRequest): LoginResponse
}

class LoginRemoteDataSourceImpl(private val client: HttpClient) : LoginRemoteDataSource {
    override suspend fun login(request: LoginRequest): LoginResponse {
        return client.post(LOGIN_URL) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}