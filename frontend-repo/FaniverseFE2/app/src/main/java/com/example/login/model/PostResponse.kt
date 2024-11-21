package com.example.login.model

import java.time.LocalDateTime

// 프론트엔드에서 사용할 데이터 모델 정의
data class PostResponse(
    val postId: Long,
    val title: String,
    val content: String?,
    val imageUrl: String?,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromEntity(post: PostResponse): PostResponse {
            return if (post.imageUrl == null) {
                PostResponse(
                    postId = post.postId,
                    title = post.title,
                    content = post.content ?: "",
                    imageUrl = null,
                    createdAt = post.createdAt
                )
            } else {
                PostResponse(
                    postId = post.postId,
                    title = post.title,
                    content = null,
                    imageUrl = post.imageUrl,
                    createdAt = post.createdAt
                )
            }
        }
    }
}
