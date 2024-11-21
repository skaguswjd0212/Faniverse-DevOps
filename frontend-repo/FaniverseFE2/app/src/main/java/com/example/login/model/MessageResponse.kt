package com.example.login.model

import java.util.Date

data class MessageResponse(
    val senderId: Long,
    val message: String?,
    val imageUrl: String?,
    val createdAt: Date
) {
    companion object {
        fun fromEntity(chatMessage: ChatMessage): MessageResponse {
            return if (chatMessage.imageLink == null) {
                MessageResponse(
                    senderId = chatMessage.senderId,
                    message = chatMessage.message ?: "",
                    imageUrl = null,
                    createdAt = chatMessage.createdAt
                )
            } else {
                MessageResponse(
                    senderId = chatMessage.senderId,
                    message = null,
                    imageUrl = chatMessage.imageLink,
                    createdAt = chatMessage.createdAt
                )
            }
        }
    }
}