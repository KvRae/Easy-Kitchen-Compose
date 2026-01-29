package com.kvrae.easykitchen.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvrae.easykitchen.domain.usecases.PingServerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Splash Screen.
 * Handles server ping to wake up the backend before user authentication.
 */
class SplashViewModel(
    private val pingServerUseCase: PingServerUseCase
) : ViewModel() {

    private val _serverPingState = MutableStateFlow<ServerPingState>(ServerPingState.Idle)
    val serverPingState: StateFlow<ServerPingState> = _serverPingState

    init {
        pingServer()
    }

    private fun pingServer() {
        viewModelScope.launch {
            _serverPingState.value = ServerPingState.Pinging

            android.util.Log.d("SplashViewModel", "Pinging server to wake it up...")

            val result = pingServerUseCase()

            _serverPingState.value = when {
                result.isSuccess -> {
                    android.util.Log.d("SplashViewModel", "Server pinged successfully")
                    ServerPingState.Success
                }

                else -> {
                    android.util.Log.d(
                        "SplashViewModel",
                        "Server ping completed (may be waking up)"
                    )
                    // We still consider it success to not block the user
                    ServerPingState.Success
                }
            }
        }
    }
}

sealed class ServerPingState {
    data object Idle : ServerPingState()
    data object Pinging : ServerPingState()
    data object Success : ServerPingState()
}
