package com.example.login.model

import java.time.LocalDateTime

data class CommentResponse(
    val content: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromEntity(comment: CommentResponse): CommentResponse {
            return CommentResponse(
                content = comment.content,
                createdAt = comment.createdAt
            )
        }
    }
}
