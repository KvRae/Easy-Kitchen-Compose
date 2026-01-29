package com.kvrae.easykitchen.domain.usecases

import com.kvrae.easykitchen.data.remote.dto.ForgetPasswordResponse
import com.kvrae.easykitchen.data.remote.dto.ResetPasswordResponse
import com.kvrae.easykitchen.data.remote.dto.VerifyOtpResponse
import com.kvrae.easykitchen.data.repository.ForgetPasswordRepository

class ForgetPasswordUseCase(private val repository: ForgetPasswordRepository) {
    suspend operator fun invoke(email: String): Result<ForgetPasswordResponse> {
        return repository.forgetPassword(email)
    }
}

class VerifyResetCodeUseCase(private val repository: ForgetPasswordRepository) {
    suspend operator fun invoke(resetCode: String, token: String): Result<VerifyOtpResponse> {
        return repository.verifyOtp(resetCode, token)
    }
}

class ResetPasswordUseCase(private val repository: ForgetPasswordRepository) {
    suspend operator fun invoke(email: String, password: String): Result<ResetPasswordResponse> {
        return repository.resetPassword(email, password)
    }
}
