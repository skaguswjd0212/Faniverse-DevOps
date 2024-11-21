package com.example.login.model

data class KeywordDto(
    val id: Long?,
    val word: String
)

data class KeywordProductDto(
    val productId: Long,
    val word: String,
    val title: String,
    val content: String,
    val category: String,
    val imageUrl: String,
    val price: Double
)
