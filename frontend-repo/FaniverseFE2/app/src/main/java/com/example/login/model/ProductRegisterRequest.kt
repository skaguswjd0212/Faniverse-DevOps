package com.example.login.model

import okhttp3.MultipartBody

open class ProductRegisterRequest(
    open val title: String,  // 상품명
    open val category: String,  // 카테고리
    open val content: String,  // 상품 설명
    open val image: MultipartBody.Part?,  // 이미지 파일
    open val imageUrl: String?  // 이미지 URL
)
