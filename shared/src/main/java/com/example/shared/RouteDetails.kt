package com.example.shared

import kotlinx.serialization.Serializable

@Serializable
class RouteDetails (
    val summary: RouteSummary,
    val description: String
)