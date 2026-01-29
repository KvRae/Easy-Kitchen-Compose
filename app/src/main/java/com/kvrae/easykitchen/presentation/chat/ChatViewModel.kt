package com.kvrae.easykitchen.presentation.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvrae.easykitchen.domain.model.MessageLimitStatus
import com.kvrae.easykitchen.domain.usecases.GeminiChatUseCase
import com.kvrae.easykitchen.domain.usecases.GetMessageLimitStatusUseCase
import com.kvrae.easykitchen.domain.usecases.TryConsumeMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val geminiChatUseCase: GeminiChatUseCase,
    private val tryConsumeMessageUseCase: TryConsumeMessageUseCase,
    private val getMessageLimitStatusUseCase: GetMessageLimitStatusUseCase
) : ViewModel() {
    private val _chatState = MutableStateFlow<ChatState>(ChatState.Idle)
    val chatState: StateFlow<ChatState> = _chatState

    private val maxDailyMessages = 5

    private val _messageLimitStatus = MutableStateFlow<MessageLimitStatus>(
        MessageLimitStatus.Allowed(remaining = maxDailyMessages, maxPerDay = maxDailyMessages)
    )
    val messageLimitStatus: StateFlow<MessageLimitStatus> = _messageLimitStatus

    var userMessage by mutableStateOf("")

    val chatMessages = mutableStateListOf<Message>()

    init {
        viewModelScope.launch {
            _messageLimitStatus.value = getMessageLimitStatusUseCase(maxDailyMessages)
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            try {
                _chatState.value = ChatState.Idle
                if (userMessage.isBlank()) {
                    _chatState.value = ChatState.Error("Please enter a message")
                    return@launch
                }

                val limitStatus = tryConsumeMessageUseCase(maxDailyMessages)
                _messageLimitStatus.value = limitStatus
                if (limitStatus is MessageLimitStatus.LimitReached) {
                    _chatState.value = ChatState.Error(
                        "You have reached the daily limit of ${limitStatus.maxPerDay} messages. Try again tomorrow."
                    )
                    return@launch
                }

                _chatState.value = ChatState.Loading

                chatMessages.add(Message(Role.USER, userMessage))
                val response = geminiChatUseCase(userMessage)

                response.fold(
                    onSuccess = { result ->
                        _chatState.value = ChatState.Success(result)
                        chatMessages.add(Message(Role.ASSISTANT, result))
                    },
                    onFailure = { error ->
                        _chatState.value =
                            ChatState.Error(error.message ?: "Failed to send message")
                    }
                )

                userMessage = ""

            } catch (e: Exception) {
                _chatState.value = ChatState.Error(e.message ?: "An unexpected error occurred")
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