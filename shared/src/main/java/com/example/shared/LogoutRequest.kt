package com.example.shared

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val token: String,
)