package com.example.routesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.routesapp.RoutesApp
import com.example.routesapp.data.StopwatchRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlin.time.Duration

class StopwatchViewModel(
    val repo: StopwatchRepository
): ViewModel() {
    private val _elapsedTime = MutableStateFlow(Duration.ZERO)

    val elapsedTime = _elapsedTime.asStateFlow()
    val isRunning: StateFlow<Boolean> = repo.startMark.transform { start ->
        emit(start != null)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )
    init {
        calcElapsed()
        launchElapsedCounter()
    }
    fun launchElapsedCounter() {
        viewModelScope.launch {
            while (repo.startMark.value != null) {
                calcElapsed()
                delay(100)
            }
        }
    }
    fun calcElapsed() {
        val elapsed = repo.startMark.value?.elapsedNow() ?: Duration.ZERO
        _elapsedTime.value = repo.accumulatedTime.value + elapsed
    }
    fun start() {
        repo.start()
        launchElapsedCounter()
    }

    fun stop() = repo.stop()

    fun toggle()  {
        if(repo.startMark.value == null) {
            start()
        } else {
            stop()
        }
    }

    fun reset() {
        repo.reset()
        _elapsedTime.value = Duration.ZERO
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                StopwatchViewModel(
                    (this[APPLICATION_KEY] as RoutesApp).appContainer.stopwatchRepository,
                )
            }
        }
    }
}