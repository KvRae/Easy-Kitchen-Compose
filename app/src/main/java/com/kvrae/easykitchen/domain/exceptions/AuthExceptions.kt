package com.kvrae.easykitchen.domain.exceptions

/**
 * Sealed class representing authentication-related exceptions in the domain layer.
 * This follows Clean Architecture principles by keeping domain exceptions separate
 * from infrastructure concerns.
 */
sealed class AuthException(message: String) : Exception(message) {

    // Login-specific exceptions
    sealed class Login(message: String) : AuthException(message) {
        class InvalidCredentials(errorMessage: String = "Invalid username or password") :
            Login(errorMessage)
        class ConnectionTimeout : Login("Connection timed out")
        class ServerUnreachable : Login("Unable to connect to server")
        class UnknownError(errorMessage: String) : Login("Error logging in: $errorMessage")
    }

    // Registration-specific exceptions
    sealed class Register(message: String) : AuthException(message) {
        class ConnectionTimeout : Register("Connection timed out")
        class ServerUnreachable : Register("Unable to connect to server")
        class UnknownError(errorMessage: String) : Register("There was an error registering the user: $errorMessage")
        class UserAlreadyExists : Register("User with this email already exists")
        class WeakPassword : Register("Password does not meet security requirements")
    }

    // Password recovery exceptions
    sealed class PasswordRecovery(message: String) : AuthException(message) {
        class InvalidEmail(errorMessage: String = "Email address is invalid") :
            PasswordRecovery(errorMessage)

        class UserNotFound(errorMessage: String = "No account found with this email") :
            PasswordRecovery(errorMessage)
        class ConnectionTimeout : PasswordRecovery("Connection timed out")
        class ServerUnreachable : PasswordRecovery("Unable to connect to server")
        class NetworkError(errorMessage: String) : PasswordRecovery(errorMessage)
        class UnknownError(errorMessage: String) : PasswordRecovery(errorMessage)
    }

    // OTP Verification exceptions
    sealed class OtpVerification(message: String) : AuthException(message) {
        class InvalidCode(errorMessage: String = "Invalid verification code") :
            OtpVerification(errorMessage)

        class ExpiredCode(errorMessage: String = "Verification code has expired") :
            OtpVerification(errorMessage)

        class VerificationFailed(errorMessage: String = "Unable to verify code") :
            OtpVerification(errorMessage)

        class ConnectionTimeout : OtpVerification("Connection timed out")
        class ServerUnreachable : OtpVerification("Unable to connect to server")
        class UnknownError(errorMessage: String) : OtpVerification(errorMessage)
    }

    // Password Reset exceptions
    sealed class PasswordReset(message: String) : AuthException(message) {
        class WeakPassword(errorMessage: String = "Invalid password. Please use at least 6 characters.") :
            PasswordReset(errorMessage)

        class AccountNotFound(errorMessage: String = "Account not found. Please try again.") :
            PasswordReset(errorMessage)

        class ConnectionTimeout : PasswordReset("Connection timed out")
        class ServerUnreachable : PasswordReset("Unable to connect to server")
        class UnknownError(errorMessage: String) : PasswordReset(errorMessage)
    }
}
