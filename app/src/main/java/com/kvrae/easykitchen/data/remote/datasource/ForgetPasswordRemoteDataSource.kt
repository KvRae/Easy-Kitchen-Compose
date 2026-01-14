package com.kvrae.easykitchen.data.remote.datasource

import com.kvrae.easykitchen.utils.FORGET_PASSWORD_URL
import com.kvrae.easykitchen.utils.RESET_PASSWORD_URL
import com.kvrae.easykitchen.utils.VERIFY_OTP_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get


interface ForgetPasswordRemoteDataSource {
    suspend fun forgetPassword(email: String)
}

class ForgetPasswordRemoteDataSourceImpl(
    private val client: HttpClient
) : ForgetPasswordRemoteDataSource {
    override suspend fun forgetPassword(email: String) {
        return client.get(FORGET_PASSWORD_URL).body()
    }

}

interface VerifyOtpRemoteDataSource {
    suspend fun verifyOtp(email: String, otp: String)
}

class VerifyOtpRemoteDataSourceImpl(
    private val client: HttpClient
) : VerifyOtpRemoteDataSource {
    override suspend fun verifyOtp(email: String, otp: String) {
        return client.get(VERIFY_OTP_URL).body()
    }

}

interface ResetPasswordRemoteDataSource {
    suspend fun resetPassword(email: String, newPassword: String)
}

class ResetPasswordRemoteDataSourceImpl(
    private val client: HttpClient
) : ResetPasswordRemoteDataSource {
    override suspend fun resetPassword(email: String, newPassword: String) {
        return client.get(RESET_PASSWORD_URL).body()
    }

}