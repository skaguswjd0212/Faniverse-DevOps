package com.example.login.model

import okhttp3.MultipartBody

data class GeneralProductRegisterRequest(
    val price: Double,
    override val title: String,
    override val category: String,
    override val content: String,
    override val imageUrl: String? = null,
    override val image: MultipartBody.Part?
) : ProductRegisterRequest(title, category, content, image, imageUrl)