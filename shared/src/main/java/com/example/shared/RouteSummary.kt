package com.example.shared

import kotlinx.serialization.Serializable

@Serializable
data class RouteSummary(
    val id: Int,
    val name: String,
    val distanceMeters: Int
)