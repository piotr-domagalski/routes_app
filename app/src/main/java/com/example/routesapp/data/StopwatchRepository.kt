package com.example.routesapp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource

open class StopwatchRepository(
) {
    private val _startMark = MutableStateFlow<TimeMark?>(null)
    private val _accumulatedTime = MutableStateFlow(Duration.ZERO)

    val startMark = _startMark.asStateFlow()
    val accumulatedTime = _accumulatedTime.asStateFlow()

    fun start() {
        _startMark.value = TimeSource.Monotonic.markNow()
    }
    fun stop() {
        val elapsed = _startMark.value?.elapsedNow() ?: Duration.ZERO
        _accumulatedTime.value += elapsed
        _startMark.value = null
    }

    fun reset() {
        _startMark.value = null
        _accumulatedTime.value = Duration.ZERO
    }
}