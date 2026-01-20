package com.kvrae.easykitchen.presentation.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvrae.easykitchen.domain.usecases.GeminiChatUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val geminiChatUseCase: GeminiChatUseCase
) : ViewModel() {
    private val _chatState = MutableStateFlow<ChatState>(ChatState.Idle)
    val chatState: StateFlow<ChatState> = _chatState


    var userMessage by mutableStateOf("")

    val chatMessages = mutableStateListOf<Message>()


    fun sendMessage() {
        viewModelScope.launch {
            try {
            _chatState.value = ChatState.Idle
            if (userMessage.isBlank()) {
                _chatState.value = ChatState.Error("Message cannot be empty")
                return@launch
            }
            val currentMessage = userMessage
            userMessage = ""
            
            _chatState.value = ChatState.Loading
            chatMessages.add(Message(Role.USER, currentMessage))

                val result = geminiChatUseCase(currentMessage)
                _chatState.value = when {
                    result.isSuccess -> {
                        val responseText = result.getOrNull() ?: "No response"
                        chatMessages.add(Message(Role.ASSISTANT, responseText))
                        ChatState.Success(responseText)
                    }

                    result.isFailure -> {
                        val errorMessage =
                            result.exceptionOrNull()?.message ?: "Failed to load data"
                        chatMessages.add(Message(Role.ASSISTANT, errorMessage))
                        ChatState.Error(errorMessage)
                    }

                    else -> {
                        chatMessages.add(Message(Role.ASSISTANT, "Content is not available"))
                        ChatState.Error("Content is not available")
                    }
                }
            } catch (e: Exception) {
                _chatState.value = ChatState.Error(e.message ?: "Failed to load data")
            }

        }
    }

    fun clearMessages() {
        chatMessages.clear()
        _chatState.value = ChatState.Idle
        userMessage = ""
    }
}

sealed class ChatState {
    data object Idle : ChatState()
    data object Loading : ChatState()
    data class Success(val response: String) : ChatState()
    data class Error(val message: String) : ChatState()
}

data class Message(
    val role: Role,
    val content: String
)

enum class Role {
    USER,
    ASSISTANT
}