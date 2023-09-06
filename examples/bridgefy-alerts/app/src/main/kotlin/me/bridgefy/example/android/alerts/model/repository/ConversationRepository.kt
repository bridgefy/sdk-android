package me.bridgefy.example.android.alerts.model.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import me.bridgefy.example.android.alerts.model.db.main.MainDatabaseWrapper
import me.bridgefy.example.android.alerts.model.db.main.messages.MessageEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationRepository
@Inject constructor(
    private val mainDatabaseWrapper: MainDatabaseWrapper,
) {

    private fun mainDatabase() = mainDatabaseWrapper.getDatabase()

    private fun chatDao() = mainDatabase().chatDao()

    fun getMessagesFlow(): Flow<List<MessageEntity>> =
        chatDao().getConversation("broadcast")

    fun handleReceivedMessage(
        message: String,
        senderName: String,
        senderId: String,
        messageId: String,
    ) {
        MainScope().launch(Dispatchers.IO) {
            chatDao().insert(
                MessageEntity(
                    id = messageId,
                    message = message,
                    receiverId = null,
                    senderId = senderId,
                    senderName = senderName,
                    date = System.currentTimeMillis().toString(),
                ),
            )
        }
    }

    fun handleSentMessage(message: String, senderId: String, messageId: String) {
        MainScope().launch(Dispatchers.IO) {
            chatDao().insert(
                MessageEntity(
                    messageId,
                    message,
                    senderId,
                    null,
                    System.currentTimeMillis().toString(),
                ),
            )
        }
    }

    fun deleteConversation() {
        MainScope().launch(Dispatchers.IO) {
            chatDao().deleteAllMessages()
        }
    }
}
