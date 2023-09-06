package me.bridgefy.example.android.alerts.ux.chat

import me.bridgefy.example.android.alerts.model.db.main.messages.MessageEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ChatUiState(
    // Data
    val getConversationFlow: StateFlow<List<MessageEntity>> = MutableStateFlow(emptyList()),
    // Events
    val onDeleteChat: () -> Unit = {},
)
