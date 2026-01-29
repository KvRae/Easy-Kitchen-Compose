package com.kvrae.easykitchen.presentation.forget_password

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvrae.easykitchen.domain.usecases.ForgetPasswordUseCase
import com.kvrae.easykitchen.domain.usecases.ResetPasswordUseCase
import com.kvrae.easykitchen.domain.usecases.VerifyResetCodeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// State classes for email input
sealed class EmailInputState {
    data object Idle : EmailInputState()
    data object Loading : EmailInputState()
    data class Success(val token: String, val message: String) : EmailInputState()
    data class Error(val message: String) : EmailInputState()
}

// State classes for OTP verification
sealed class OtpVerificationState {
    data object Idle : OtpVerificationState()
    data object Loading : OtpVerificationState()
    data object Success : OtpVerificationState()
    data class Error(val message: String) : OtpVerificationState()
}

// State classes for password reset
sealed class PasswordResetState {
    data object Idle : PasswordResetState()
    data object Loading : PasswordResetState()
    data object Success : PasswordResetState()
    data class Error(val message: String) : PasswordResetState()
}

class ForgetPasswordViewModel(
    private val forgetPasswordUseCase: ForgetPasswordUseCase,
    private val verifyResetCodeUseCase: VerifyResetCodeUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _emailInputState = MutableStateFlow<EmailInputState>(EmailInputState.Idle)
    val emailInputState: StateFlow<EmailInputState> = _emailInputState

    private val _otpVerificationState =
        MutableStateFlow<OtpVerificationState>(OtpVerificationState.Idle)
    val otpVerificationState: StateFlow<OtpVerificationState> = _otpVerificationState

    private val _passwordResetState = MutableStateFlow<PasswordResetState>(PasswordResetState.Idle)
    val passwordResetState: StateFlow<PasswordResetState> = _passwordResetState

    // Store email and token for use in subsequent steps
    private var storedEmail: String = ""
    private var storedToken: String = ""

    fun requestPasswordReset(email: String) {
        viewModelScope.launch {
            try {
                Log.d("ForgetPasswordViewModel", "Requesting password reset for email: $email")
                _emailInputState.value = EmailInputState.Loading

                val result = forgetPasswordUseCase(email)

                if (result.isSuccess) {
                    val response = result.getOrNull()
                    storedEmail = email
                    storedToken = response?.token ?: ""
                    Log.d(
                        "ForgetPasswordViewModel",
                        "Reset email sent successfully. Token: $storedToken"
                    )
                    _emailInputState.value = EmailInputState.Success(
                        token = storedToken,
                        message = response?.message ?: "Reset code sent to your email"
                    )
                } else {
                    val errorMessage =
                        result.exceptionOrNull()?.message ?: "Failed to send reset code"
                    Log.e("ForgetPasswordViewModel", "Error: $errorMessage")
                    _emailInputState.value = EmailInputState.Error(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("ForgetPasswordViewModel", "Exception: ${e.message}", e)
                _emailInputState.value = EmailInputState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun verifyResetCode(resetCode: String) {
        viewModelScope.launch {
            try {
                Log.d("ForgetPasswordViewModel", "Verifying reset code: $resetCode")
                _otpVerificationState.value = OtpVerificationState.Loading

                val result = verifyResetCodeUseCase(resetCode, storedToken)

                if (result.isSuccess) {
                    Log.d("ForgetPasswordViewModel", "Reset code verified successfully")
                    _otpVerificationState.value = OtpVerificationState.Success
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Invalid reset code"
                    Log.e("ForgetPasswordViewModel", "Error: $errorMessage")
                    _otpVerificationState.value = OtpVerificationState.Error(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("ForgetPasswordViewModel", "Exception: ${e.message}", e)
                _otpVerificationState.value =
                    OtpVerificationState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun resetPassword(newPassword: String) {
        viewModelScope.launch {
            try {
                Log.d("ForgetPasswordViewModel", "Resetting password for email: $storedEmail")
                _passwordResetState.value = PasswordResetState.Loading

                val result = resetPasswordUseCase(storedEmail, newPassword)

                if (result.isSuccess) {
                    Log.d("ForgetPasswordViewModel", "Password reset successfully")
                    _passwordResetState.value = PasswordResetState.Success
                } else {
                    val errorMessage =
                        result.exceptionOrNull()?.message ?: "Failed to reset password"
                    Log.e("ForgetPasswordViewModel", "Error: $errorMessage")
                    _passwordResetState.value = PasswordResetState.Error(errorMessage)
                }
            } catch (e: Exception) {
                Log.e("ForgetPasswordViewModel", "Exception: ${e.message}", e)
                _passwordResetState.value =
                    PasswordResetState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun resetState() {
        _emailInputState.value = EmailInputState.Idle
        _otpVerificationState.value = OtpVerificationState.Idle
        _passwordResetState.value = PasswordResetState.Idle
        storedEmail = ""
        storedToken = ""
    }
}
