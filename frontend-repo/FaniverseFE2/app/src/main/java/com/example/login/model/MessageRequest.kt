package com.example.login.model

import java.util.Date

data class MessageRequest(
    val userId: Long,
    val message: String?,
    val imageUrl: String?
) {
    fun toEntity(senderId: Long, chatRoomId: Long): ChatMessage {
        val createdAt = Date() // 현재 시간을 Date 객체로 생성

        return if (imageUrl == null) {
            ChatMessage(
                senderId = senderId,
                chatRoomId = chatRoomId,
                message = message ?: "",
                imageLink = null,
                messageType = MessageType.TEXT,
                createdAt = createdAt // Date 객체를 사용
            )
        } else {
            ChatMessage(
                senderId = senderId,
                chatRoomId = chatRoomId,
                message = null,
                imageLink = imageUrl,
                messageType = MessageType.IMAGE,
                createdAt = createdAt // Date 객체를 사용
            )
        }
    }
}
