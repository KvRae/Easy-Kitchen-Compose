package com.kvrae.easykitchen.presentation.forget_password

/**
 * Validation utilities for forgot password screens
 */
object ForgotPasswordValidator {
    private const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$"

    fun isValidEmail(email: String): Boolean {
        val trimmed = email.trim()
        return trimmed.isNotEmpty() && trimmed.matches(EMAIL_REGEX.toRegex())
    }

    fun getEmailError(email: String): String? {
        return when {
            email.isBlank() -> null
            !isValidEmail(email) -> "Please enter a valid email address"
            else -> null
        }
    }

    fun sanitizeEmail(email: String): String = email.trim().lowercase()

    fun isValidOtp(otp: String): Boolean {
        val trimmed = otp.trim()
        return trimmed.length == 6 && trimmed.all { it.isDigit() }
    }

    fun getOtpError(otp: String): String? {
        return when {
            otp.isBlank() -> null
            !otp.all { it.isDigit() || it.isWhitespace() } -> "Only numbers allowed"
            otp.trim().length < 6 -> "Code must be 6 digits"
            otp.trim().length > 6 -> "Code must be exactly 6 digits"
            else -> null
        }
    }

    fun sanitizeOtp(otp: String): String {
        return otp.filter { it.isDigit() }.take(6)
    }
}
