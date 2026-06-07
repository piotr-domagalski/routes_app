package com.example.routesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class StopwatchViewModel: ViewModel() {
    private val _startMark = MutableStateFlow<TimeMark?>(null)
    private val _accumulatedTime = MutableStateFlow(Duration.ZERO)
    private val _elapsedTime = MutableStateFlow(Duration.ZERO)

    val elapsedTime = _elapsedTime.asStateFlow()
    val isRunning: StateFlow<Boolean> = _startMark.transform {
        start -> emit(start != null)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )
    fun start() {
        _startMark.value = TimeSource.Monotonic.markNow()

        viewModelScope.launch {
            while(_startMark.value != null) {
                val elapsed = _startMark.value?.elapsedNow() ?: Duration.ZERO
                _elapsedTime.value = _accumulatedTime.value + elapsed
                delay(100)
            }
        }
    }
    fun stop() {
        val elapsed = _startMark.value?.elapsedNow() ?: Duration.ZERO
        _accumulatedTime.value += elapsed
        _startMark.value = null
    }

    fun toggle()  {
        if(_startMark.value == null) {
            start()
        } else {
            stop()
        }
    }
    fun reset() {
        _startMark.value = null
        _accumulatedTime.value = Duration.ZERO
        _elapsedTime.value = Duration.ZERO
    }
}