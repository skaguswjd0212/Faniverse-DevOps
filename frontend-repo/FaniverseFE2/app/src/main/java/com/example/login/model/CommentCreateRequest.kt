package com.example.login.model

data class CommentCreateRequest(
    val content: String
) {
    fun toEntity(): CommentCreateRequest {
        return CommentCreateRequest(
            content = this.content
        )
    }
}