package me.bridgefy.example.android.alerts.model.db.main.messages

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Messages")
data class MessageEntity(
    @PrimaryKey
    var id: String,
    var message: String? = null,
    var receiverId: String? = null,
    var senderId: String? = null,
    var senderName: String? = null,
    var date: String? = null,
    var chatRoomId: String? = "broadcast",
)
