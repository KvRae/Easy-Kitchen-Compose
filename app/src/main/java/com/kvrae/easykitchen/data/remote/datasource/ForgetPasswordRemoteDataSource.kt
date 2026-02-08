package com.kvrae.easykitchen.data.remote.datasource

import com.kvrae.easykitchen.data.remote.dto.ForgetPasswordRequest
import com.kvrae.easykitchen.data.remote.dto.ForgetPasswordResponse
import com.kvrae.easykitchen.data.remote.dto.ResetPasswordRequest
import com.kvrae.easykitchen.data.remote.dto.ResetPasswordResponse
import com.kvrae.easykitchen.data.remote.dto.VerifyOtpResponse
import com.kvrae.easykitchen.data.remote.utils.ErrorMapper
import com.kvrae.easykitchen.domain.exceptions.AuthException
import com.kvrae.easykitchen.utils.FORGET_PASSWORD_URL
import com.kvrae.easykitchen.utils.RESET_PASSWORD_URL
import com.kvrae.easykitchen.utils.VERIFY_OTP_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

interface ForgetPasswordRemoteDataSource {
    suspend fun forgetPassword(email: String): ForgetPasswordResponse
}

class ForgetPasswordRemoteDataSourceImpl(
    private val client: HttpClient
) : ForgetPasswordRemoteDataSource {

    override suspend fun forgetPassword(email: String): ForgetPasswordResponse {
        return try {
            val response = client.post(FORGET_PASSWORD_URL) {
                contentType(ContentType.Application.Json)
                setBody(ForgetPasswordRequest(email = email))
            }
            if (response.status.isSuccess()) {
                response.body()
            } else {
                throw ErrorMapper.mapApiException(
                    response.bodyAsText(),
                    response.status.value,
                    ErrorMapper.ErrorContext.FORGET_PASSWORD
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("ForgetPasswordDataSource", "Error sending reset code", e)

            when (e) {
                is AuthException -> throw e
                else -> throw ErrorMapper.mapNetworkException(
                    e,
                    ErrorMapper.ErrorContext.FORGET_PASSWORD
                )
            }
        }
    }
}

interface VerifyOtpRemoteDataSource {
    suspend fun verifyOtp(resetCode: String, token: String): VerifyOtpResponse
}

class VerifyOtpRemoteDataSourceImpl(
    private val client: HttpClient
) : VerifyOtpRemoteDataSource {

    override suspend fun verifyOtp(resetCode: String, token: String): VerifyOtpResponse {
        return try {
            val response = client.post(VERIFY_OTP_URL) {
                contentType(ContentType.Application.Json)
                setBody(mapOf("resetCode" to resetCode, "token" to token))
            }
            if (response.status.isSuccess()) {
                response.body()
            } else {
                throw ErrorMapper.mapApiException(
                    response.bodyAsText(),
                    response.status.value,
                    ErrorMapper.ErrorContext.VERIFY_OTP
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("VerifyOtpDataSource", "Error verifying code", e)

            when (e) {
                is AuthException -> throw e
                else -> throw ErrorMapper.mapNetworkException(
                    e,
                    ErrorMapper.ErrorContext.VERIFY_OTP
                )
            }
        }
    }
}

interface ResetPasswordRemoteDataSource {
    suspend fun resetPassword(email: String, password: String): ResetPasswordResponse
}

class ResetPasswordRemoteDataSourceImpl(
    private val client: HttpClient
) : ResetPasswordRemoteDataSource {

    override suspend fun resetPassword(email: String, password: String): ResetPasswordResponse {
        return try {
            val response = client.post(RESET_PASSWORD_URL) {
                contentType(ContentType.Application.Json)
                setBody(ResetPasswordRequest(email = email, password = password))
            }
            if (response.status.isSuccess()) {
                response.body()
            } else {
                throw ErrorMapper.mapApiException(
                    response.bodyAsText(),
                    response.status.value,
                    ErrorMapper.ErrorContext.RESET_PASSWORD
                )
            }
        } catch (e: Exception) {
            android.util.Log.e("ResetPasswordDataSource", "Error resetting password", e)

            when (e) {
                is AuthException -> throw e
                else -> throw ErrorMapper.mapNetworkException(
                    e,
                    ErrorMapper.ErrorContext.RESET_PASSWORD
                )
            }
        }
    }
}

