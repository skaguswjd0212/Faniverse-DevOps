package com.example.login.model

import okhttp3.MultipartBody
import java.time.LocalDateTime

data class AuctionProductRegisterRequest(
    val startingPrice: Double,
    val endDate: LocalDateTime,
    override val title: String,
    override val category: String,
    override val content: String,
    override val image: MultipartBody.Part?,
    override val imageUrl: String? = null
) : ProductRegisterRequest(title, category, content, image, imageUrl)