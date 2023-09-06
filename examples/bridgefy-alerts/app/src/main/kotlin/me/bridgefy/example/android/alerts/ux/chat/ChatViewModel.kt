package me.bridgefy.example.android.alerts.ux.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import me.bridgefy.example.android.alerts.model.repository.ConversationRepository
import me.bridgefy.example.android.alerts.ui.navigation.ViewModelNav
import me.bridgefy.example.android.alerts.ui.navigation.ViewModelNavImpl
import me.bridgefy.example.android.alerts.util.ext.stateInDefault
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
@Inject constructor(
    conversationRepository: ConversationRepository,
) : ViewModel(), ViewModelNav by ViewModelNavImpl() {
    val uiState = ChatUiState(
        getConversationFlow = conversationRepository.getMessagesFlow().stateInDefault(viewModelScope, emptyList()),
        onDeleteChat = { conversationRepository.deleteConversation() },
    )
}
