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
        class InvalidEmail : PasswordRecovery("Email address is invalid")
        class UserNotFound : PasswordRecovery("No account found with this email")
        class ConnectionTimeout : PasswordRecovery("Connection timed out")
        class ServerUnreachable : PasswordRecovery("Unable to connect to server")
    }
}

