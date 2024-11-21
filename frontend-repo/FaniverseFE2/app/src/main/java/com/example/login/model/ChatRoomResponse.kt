package com.example.login.model

import java.time.LocalDateTime

data class ChatRoomResponse(
    val chatRoomId: Long,
    val buyerId: Long,
    val buyerNickname: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromEntity(chatRoom: ChatRoomResponse): ChatRoomResponse {
            return ChatRoomResponse(
                chatRoomId = chatRoom.chatRoomId,
                buyerId = chatRoom.buyerId,
                buyerNickname = chatRoom.buyerNickname,
                createdAt = chatRoom.createdAt
            )
        }
    }
}
