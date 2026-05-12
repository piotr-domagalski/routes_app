package com.example.shared

import kotlinx.serialization.Serializable

@Serializable
data class RouteDetails (
    val summary: RouteSummary,
    val description: String
)
