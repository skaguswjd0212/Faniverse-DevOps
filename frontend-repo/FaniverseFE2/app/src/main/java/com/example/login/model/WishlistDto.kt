package com.example.login.model

data class WishlistProductDto(
    val wishlistId: Long,
    val userId: Long,
    val productId: Long,
    val title: String,
    val content: String,
    val category: String,
    val imageUrl: String,
    val price: Double
)

data class WishlistRequestDto(
    val productId: Long
)
