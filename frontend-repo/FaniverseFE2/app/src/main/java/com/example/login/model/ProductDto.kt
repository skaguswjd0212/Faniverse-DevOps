package com.example.login.model

import java.time.LocalDateTime

data class ProductDto(
    val id: Long,
    val title: String,
    val content: String,
    val category: String,
    val imageUrl: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
