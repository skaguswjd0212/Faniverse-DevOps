package com.example.login.model

import okhttp3.MultipartBody

data class GeneralProductUpdateRequest(
    val title: String,
    val category: String,
    val content: String,
    val price: Double,
    val image: MultipartBody.Part?,
    val sellerId: Long?,
    val imageUrl: String?
)