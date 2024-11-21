package com.example.login.model

import java.time.LocalDateTime

data class ProductListResponse(
    val id: Long,  // 상품 ID (클릭 시 상세 조회용)
    val title: String,  // 상품 제목
    val imageUrl: String,  // 상품 이미지
    val price: Double?,  // 상품 가격 (일반 상품일 경우)
    val startingPrice: Double?,  // 경매 시작 가격
    val finalPrice: Double?,  // 경매 최종 가격
    val endDate: LocalDateTime?,  // 경매 종료일 (경매 상품일 경우)
    val category: String  // 카테고리
)
