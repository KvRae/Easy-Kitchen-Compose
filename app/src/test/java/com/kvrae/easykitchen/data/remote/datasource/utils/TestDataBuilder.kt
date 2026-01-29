package com.kvrae.easykitchen.data.remote.datasource.utils

import com.kvrae.easykitchen.data.remote.dto.User

/**
 * Test data builders and utilities for data layer tests
 *
 * This object provides reusable test data and builders following the Builder pattern.
 * This improves test readability and reduces duplication across test files.
 */
object TestDataBuilder {

    /**
     * Create a test User with optional overrides
     */
    fun createTestUser(
        id: String = "123",
        username: String = "testuser",
        email: String = "test@example.com",
        password: String? = null,
        phone: String? = null
    ) = User(
        id = id,
        username = username,
        email = email,
        password = password,
        phone = phone
    )

    /**
     * User factory methods for common test scenarios
     */
    object Users {
        fun validTestUser() = createTestUser()

        fun withEmail(email: String) = createTestUser(email = email)

        fun withUsername(username: String) = createTestUser(username = username)

        fun withPhone(phone: String) = createTestUser(phone = phone)

        fun minimal() = User(
            username = "user",
            email = "user@example.com"
        )

        fun complete() = createTestUser(
            id = "full-id",
            username = "fulluser",
            email = "full@example.com",
            phone = "1234567890"
        )
    }
}

/**
 * Test constants for common test values
 */
object TestConstants {
    const val TEST_USERNAME = "testuser"
    const val TEST_EMAIL = "test@example.com"
    const val TEST_PASSWORD = "TestPassword123!"
    const val TEST_USER_ID = "user-123"
    const val TEST_PHONE = "+1234567890"

    const val VALID_EMAIL = "valid@example.com"
    const val INVALID_EMAIL = "invalid.email"
    const val SPECIAL_CHAR_EMAIL = "test+tag@example.co.uk"

    const val WEAK_PASSWORD = "123"
    const val STRONG_PASSWORD = "StrongPass123!@#"

    const val SPECIAL_CHAR_USERNAME = "user.name_123"
    const val UNICODE_USERNAME = "用户"

    // Error messages that may be returned from API
    const val USER_EXISTS_ERROR = "User already exists with this username or email"
    const val WEAK_PASSWORD_ERROR = "Password must be at least 6 characters long"
    const val INVALID_EMAIL_ERROR = "Invalid email format"
    const val INTERNAL_SERVER_ERROR = "Internal server error"
}

/**
 * Assertion helper functions for common test validations
 */
object TestAssertions {

    fun assertIsUserAlreadyExistsException(exception: Exception?): Boolean {
        return exception?.message?.contains("already exists", ignoreCase = true) == true
    }

    fun assertIsWeakPasswordException(exception: Exception?): Boolean {
        return exception?.message?.contains("password", ignoreCase = true) == true
    }

    fun assertIsConnectionTimeoutException(exception: Exception?): Boolean {
        return exception?.message?.contains("timeout", ignoreCase = true) == true
    }

    fun assertIsServerUnreachableException(exception: Exception?): Boolean {
        val message = exception?.message ?: return false
        return message.contains("Unable to resolve host", ignoreCase = true) ||
                message.contains("Connection refused", ignoreCase = true) ||
                message.contains("Failed to connect", ignoreCase = true)
    }
}
