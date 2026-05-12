package com.example.shared

import kotlinx.serialization.Serializable

@Serializable
class RouteSummary(
    val id: Int,
    val name: String,
    val distanceMeters: Int
)
