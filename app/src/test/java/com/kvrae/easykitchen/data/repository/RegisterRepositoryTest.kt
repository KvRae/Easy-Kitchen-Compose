package com.kvrae.easykitchen.data.repository

import android.util.Log
import com.kvrae.easykitchen.data.remote.datasource.RegisterRemoteDataSource
import com.kvrae.easykitchen.data.remote.dto.RegisterRequest
import com.kvrae.easykitchen.data.remote.dto.RegisterResponse
import com.kvrae.easykitchen.data.remote.dto.User
import com.kvrae.easykitchen.domain.exceptions.AuthException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.net.ConnectException

/**
 * Unit tests for RegisterRepository
 *
 * Tests the RegisterRepositoryImpl class by mocking the remote data source.
 */
class RegisterRepositoryTest {

    private lateinit var mockRemoteDataSource: RegisterRemoteDataSource
    private lateinit var registerRepository: RegisterRepository

    companion object {
        private val testRegisterRequest = RegisterRequest(
            username = "testuser",
            email = "test@example.com",
            password = "password123"
        )

        private val testUser = User(
            id = "123",
            username = "testuser",
            email = "test@example.com"
        )

        private val testSuccessResponse = RegisterResponse(
            user = testUser,
            message = "Account created successfully"
        )

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            // Mock Android's Log class to prevent "not mocked" errors in unit tests
            mockkStatic(Log::class)
            every { Log.d(any(), any()) } returns 0
            every { Log.d(any(), any(), any()) } returns 0
            every { Log.e(any(), any()) } returns 0
            every { Log.e(any(), any(), any()) } returns 0
        }
    }

    @Before
    fun setup() {
        mockRemoteDataSource = mockk()
        registerRepository = RegisterRepositoryImpl(mockRemoteDataSource)
    }

    /**
     * Test successful registration
     */
    @Test
    fun `register returns success result when data source succeeds`() = runTest {
        coEvery {
            mockRemoteDataSource.register(testRegisterRequest)
        } returns testSuccessResponse

        val result = registerRepository.register(testRegisterRequest)

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        assertEquals(testSuccessResponse, result.getOrNull())
    }

    /**
     * Test socket timeout is wrapped in UnknownError
     * Note: The repository catches all non-AuthException exceptions and wraps them
     */
    @Test
    fun `register returns failure with UnknownError on socket timeout`() = runTest {
        coEvery {
            mockRemoteDataSource.register(testRegisterRequest)
        } throws SocketTimeoutException("Connection timed out")

        val result = registerRepository.register(testRegisterRequest)

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is AuthException.Register.UnknownError)
    }

    /**
     * Test connection exception is wrapped in UnknownError
     * Note: The repository catches all non-AuthException exceptions and wraps them
     */
    @Test
    fun `register returns failure with UnknownError on connection error`() = runTest {
        coEvery {
            mockRemoteDataSource.register(testRegisterRequest)
        } throws ConnectException("Connection refused")

        val result = registerRepository.register(testRegisterRequest)

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is AuthException.Register.UnknownError)
        assertTrue(exception?.message?.contains("Connection refused") == true)
    }

    /**
     * Test generic exception is converted to UnknownError
     */
    @Test
    fun `register returns failure with UnknownError for generic exception`() = runTest {
        val errorMessage = "Something went wrong"
        coEvery {
            mockRemoteDataSource.register(testRegisterRequest)
        } throws RuntimeException(errorMessage)

        val result = registerRepository.register(testRegisterRequest)

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is AuthException.Register.UnknownError)
        assertTrue(exception?.message?.contains(errorMessage) == true)
    }

    /**
     * Test UserAlreadyExists exception propagation
     */
    @Test
    fun `register returns failure when data source throws UserAlreadyExists`() = runTest {
        coEvery {
            mockRemoteDataSource.register(testRegisterRequest)
        } throws AuthException.Register.UserAlreadyExists()

        val result = registerRepository.register(testRegisterRequest)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is AuthException.Register.UserAlreadyExists)
    }

    /**
     * Test result is wrapped in success
     */
    @Test
    fun `register wraps successful response in Result success`() = runTest {
        coEvery {
            mockRemoteDataSource.register(testRegisterRequest)
        } returns testSuccessResponse

        val result = registerRepository.register(testRegisterRequest)

        assertTrue(result.isSuccess)
        assertTrue(!result.isFailure)
        assertNotNull(result.getOrNull())
    }

    /**
     * Test getOrNull returns data on success
     */
    @Test
    fun `register getOrNull returns response on success`() = runTest {
        coEvery {
            mockRemoteDataSource.register(testRegisterRequest)
        } returns testSuccessResponse

        val result = registerRepository.register(testRegisterRequest)

        assertNotNull(result.getOrNull())
        assertEquals(testSuccessResponse, result.getOrNull())
    }
}
