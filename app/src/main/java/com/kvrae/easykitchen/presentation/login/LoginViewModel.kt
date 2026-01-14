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
                _loginState.value = LoginState.Error("Username cannot be empty")
                return@launch
            }
            if (password.isBlank()) {
                _loginState.value = LoginState.Error("Password cannot be empty")
                return@launch
            }

            _loginState.value = LoginState.Loading
            val result = loginUseCase(LoginRequest(username.trim(), password.trim()))
            _loginState.value = when {
                result.isSuccess -> {
                    setLoggedInState()
                    LoginState.Success(result.getOrNull()!!)
                }
                result.isFailure -> {
                    LoginState.Error(result.exceptionOrNull()?.message ?: "Failed to load data")
                }
                else -> {
                    LoginState.Error("Content is not available")
                }
            }
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