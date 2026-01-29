package com.kvrae.easykitchen.presentation.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvrae.easykitchen.data.remote.dto.LoginRequest
import com.kvrae.easykitchen.data.remote.dto.LoginResponse
import com.kvrae.easykitchen.domain.usecases.LoginUseCase
import com.kvrae.easykitchen.utils.UserPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    val userName = mutableStateOf("")
    val password = mutableStateOf("")

    val rememberMe = mutableStateOf(false)

    init {
        viewModelScope.launch {
            userPreferencesManager.isLoggedIn.collectLatest { loggedIn ->
                _isLoggedIn.value = loggedIn
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            // Validate inputs before setting Loading state
            if (username.isBlank()) {
                _loginState.value = LoginState.Error("Username or email cannot be empty")
                return@launch
            }
            if (password.isBlank()) {
                _loginState.value = LoginState.Error("Password cannot be empty")
                return@launch
            }

            android.util.Log.d("LoginViewModel", "Starting login for user: $username")
            _loginState.value = LoginState.Loading
            android.util.Log.d("LoginViewModel", "State set to Loading")

            // Detect if input is email or username
            val isEmail = username.contains("@")
            val loginRequest = if (isEmail) {
                LoginRequest(email = username.trim(), password = password.trim())
            } else {
                LoginRequest(username = username.trim(), password = password.trim())
            }

            val result = loginUseCase(loginRequest)
            android.util.Log.d("LoginViewModel", "Login result received: ${result.isSuccess}")

            _loginState.value = when {
                result.isSuccess -> {
                    android.util.Log.d(
                        "LoginViewModel",
                        "Login successful, setting logged in state"
                    )
                    setLoggedInState()
                    LoginState.Success(result.getOrNull()!!)
                }
                result.isFailure -> {
                    android.util.Log.d(
                        "LoginViewModel",
                        "Login failed: ${result.exceptionOrNull()?.message}"
                    )
                    LoginState.Error(result.exceptionOrNull()?.message ?: "Failed to load data")
                }
                else -> {
                    android.util.Log.d("LoginViewModel", "Login returned unexpected result")
                    LoginState.Error("Content is not available")
                }
            }
            android.util.Log.d("LoginViewModel", "Final state: ${_loginState.value}")
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    fun onRememberMeChanged() {
        rememberMe.value = !rememberMe.value
    }

    fun setLoggedInState() {
        if (rememberMe.value) {
            viewModelScope.launch {
                userPreferencesManager.saveLoginState(true)
                userPreferencesManager.saveUsername(userName.value.trim())
            }
        }
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val response: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}