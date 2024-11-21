package com.example.login.model

data class RegisterRequestDto(
    val email: String,
    val username: String,
    val password: String
)