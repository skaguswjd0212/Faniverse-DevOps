package com.example.login.model

data class AuctionBidResponse(
    val id: Long,
    val bidAmount: Double,
    val bidderName: String
)