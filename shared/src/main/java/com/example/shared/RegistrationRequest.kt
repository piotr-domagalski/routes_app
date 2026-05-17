package com.example.shared

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val username: String,
    val displayName: String?,
    val password: String
)