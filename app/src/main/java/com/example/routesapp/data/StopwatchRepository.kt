package com.example.routesapp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@OptIn(ExperimentalTime::class)
open class StopwatchRepository(
) {
    private val _startInstant = MutableStateFlow<Instant?>(null)
    private val _startMark = MutableStateFlow<TimeMark?>(null)
    private val _accumulatedTime = MutableStateFlow(Duration.ZERO)

    val startInstant = _startInstant.asStateFlow()
    val startMark = _startMark.asStateFlow()
    val accumulatedTime = _accumulatedTime.asStateFlow()

    fun start() {
        _startInstant.value = _startInstant.value?:Clock.System.now()
        _startMark.value = TimeSource.Monotonic.markNow()
    }
    fun stop() {
        val elapsed = _startMark.value?.elapsedNow() ?: Duration.ZERO
        _accumulatedTime.value += elapsed
        _startMark.value = null
    }

    fun reset() {
        _startInstant.value = null
        _startMark.value = null
        _accumulatedTime.value = Duration.ZERO
    }
}