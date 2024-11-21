package com.example.login.model

data class PostCreateRequest(
    val title: String,
    val content: String,
    val imageUrl: String? = null
)
    