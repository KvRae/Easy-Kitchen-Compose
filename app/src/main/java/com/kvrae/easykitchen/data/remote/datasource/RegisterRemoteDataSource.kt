package com.kvrae.easykitchen.data.remote.datasource

import com.kvrae.easykitchen.data.remote.dto.RegisterRequest
import com.kvrae.easykitchen.data.remote.dto.RegisterResponse
import com.kvrae.easykitchen.utils.REGISTER_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface RegisterRemoteDataSource {
    suspend fun register(request: RegisterRequest): RegisterResponse

}

class RegisterRemoteDataSourceImpl(private val client: HttpClient) : RegisterRemoteDataSource {
    override suspend fun register(request: RegisterRequest): RegisterResponse {
        return client.post(REGISTER_URL) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    }

}