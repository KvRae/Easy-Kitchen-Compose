package com.kvrae.easykitchen.presentation.login

import android.app.Application
import android.util.Log
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvrae.easykitchen.data.remote.dto.User
import com.kvrae.easykitchen.domain.usecases.BuildGoogleCredentialRequestUseCase
import com.kvrae.easykitchen.domain.usecases.HandleGoogleCredentialResultUseCase
import com.kvrae.easykitchen.utils.UserPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GoogleAuthViewModel(
    private val buildGoogleCredentialRequestUseCase: BuildGoogleCredentialRequestUseCase,
    private val handleGoogleCredentialResultUseCase: HandleGoogleCredentialResultUseCase,
    private val userPreferencesManager: UserPreferencesManager,
    private val application: Application
) : ViewModel() {
    private val _googleAuthState = MutableStateFlow<GoogleAuthState>(GoogleAuthState.Idle)
    val googleAuthState: StateFlow<GoogleAuthState> = _googleAuthState

    fun signIn(serverClientId: String) {
        viewModelScope.launch {
            try {
                Log.d("GoogleAuthViewModel", "=== SIGN IN FLOW STARTED ===")
                Log.d(
                    "GoogleAuthViewModel",
                    "Starting Google sign in with clientId: $serverClientId"
                )

                // Proactively clear any cached credential state to avoid reauth loops
                Log.d("GoogleAuthViewModel", "Clearing cached credential state before sign-in...")
                try {
                    androidx.credentials.CredentialManager.create(application)
                        .clearCredentialState(androidx.credentials.ClearCredentialStateRequest())
                    Log.d("GoogleAuthViewModel", "Credential state cleared successfully")
                } catch (e: Exception) {
                    Log.w(
                        "GoogleAuthViewModel",
                        "Failed to clear credential state (non-fatal): ${e.message}"
                    )
                }

                _googleAuthState.value = GoogleAuthState.Loading
                Log.d("GoogleAuthViewModel", "State set to Loading")

                val request: GetCredentialRequest =
                    buildGoogleCredentialRequestUseCase(serverClientId)
                Log.d(
                    "GoogleAuthViewModel",
                    "Credential request built successfully with fresh nonce"
                )

                Log.d("GoogleAuthViewModel", "Calling handleGoogleCredentialResultUseCase...")
                val result = handleGoogleCredentialResultUseCase(request)
                Log.d(
                    "GoogleAuthViewModel",
                    "Credential result received: isSuccess=${result.isSuccess}"
                )

                if (result.isSuccess) {
                    val user = result.getOrNull() ?: User()
                    Log.d(
                        "GoogleAuthViewModel",
                        "User retrieved - email: ${user.email}, username: ${user.username}"
                    )
                    // Persist minimal data (ID token placeholder stored as email; replace when backend is available)
                    userPreferencesManager.saveUsername(user.username ?: "")
                    userPreferencesManager.saveEmail(user.email ?: "")
                    userPreferencesManager.saveLoginState(true)
                    Log.d("GoogleAuthViewModel", "User data saved to preferences")
                    _googleAuthState.value = GoogleAuthState.Success(user)
                    Log.d("GoogleAuthViewModel", "State set to Success")
                } else {
                    val ex = result.exceptionOrNull()
                    Log.e(
                        "GoogleAuthViewModel",
                        "Result failed with exception: ${ex?.javaClass?.simpleName}",
                        ex
                    )
                    if (ex is GetCredentialCancellationException) {
                        Log.w("GoogleAuthViewModel", "Sign-in cancelled or reauth required", ex)
                        // Show error message for persistent reauth issues
                        _googleAuthState.value = GoogleAuthState.Error(
                            "Google Sign-In unavailable. Try regular login."
                        )
                        Log.d("GoogleAuthViewModel", "State set to Error - server/auth issue")
                        return@launch
                    }
                    val message = when (ex) {
                        is GetCredentialException -> "Auth service unavailable. Try again later."
                        else -> ex?.message ?: "Google sign-in failed"
                    }
                    Log.e("GoogleAuthViewModel", "Sign in error: $message", ex)
                    _googleAuthState.value = GoogleAuthState.Error(message)
                    Log.d("GoogleAuthViewModel", "State set to Error with message: $message")
                }
            } catch (e: Exception) {
                Log.e("GoogleAuthViewModel", "=== EXCEPTION IN SIGN IN FLOW ===", e)
                if (e is GetCredentialCancellationException) {
                    Log.w("GoogleAuthViewModel", "Sign-in cancelled or reauth required", e)
                    // Show error message for persistent reauth issues
                    _googleAuthState.value = GoogleAuthState.Error(
                        "Google Sign-In unavailable. Try regular login."
                    )
                    Log.d("GoogleAuthViewModel", "State set to Error - server/auth issue")
                    return@launch
                }
                Log.e("GoogleAuthViewModel", "Sign in failed with exception: ${e.message}", e)
                val message = when (e) {
                    is GetCredentialException -> "Auth service unavailable. Try again later."
                    else -> e.message ?: "Google sign-in failed"
                }
                _googleAuthState.value = GoogleAuthState.Error(message)
                Log.d("GoogleAuthViewModel", "State set to Error with message: $message")
            }
        }
    }

    fun resetGoogleAuthState() {
        _googleAuthState.value = GoogleAuthState.Idle
    }
}

sealed class GoogleAuthState {
    data object Idle : GoogleAuthState()
    data object Loading : GoogleAuthState()
    data class Success(val user: User) : GoogleAuthState()
    data class Error(val message: String) : GoogleAuthState()
}