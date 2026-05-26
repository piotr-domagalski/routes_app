package com.example.shared

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class WorkoutSummary(
    val user: String,
    val route: Int,
    val timestamp: Instant,
    val duration: LocalTime,
    val private: Boolean
)
