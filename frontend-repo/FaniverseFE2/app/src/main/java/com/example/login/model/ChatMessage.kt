package com.example.login.model

import java.util.Date

data class ChatMessage(
    val senderId: Long,
    val chatRoomId: Long,
    val message: String?,
    val imageLink: String?,
    val messageType: MessageType,
    val createdAt: Date
)