package me.bridgefy.example.android.alerts.model.data

import kotlinx.serialization.Serializable

@Serializable
data class MessagePayload(
    val id: Long? = null,
    val command: Int,
    var ct: MessageContentPayload? = null,
    val image: Int? = null,
    val color: String? = null,
    val text: String? = null,
)

@Serializable
data class MessageContentPayload(
    val date: Long,
    val text: String,
    val username: String,
    val platform: Int,
)

enum class Commands {
    IMAGE,
    COLOR,
    FLASHLIGHT,
    TEXT,
    CHAT,
    SOUND,
}
