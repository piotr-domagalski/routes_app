package com.example.routesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.routesapp.RoutesApp
import com.example.routesapp.data.AuthRepository
import com.example.routesapp.data.RoutesRepository
import com.example.routesapp.data.SessionState
import com.example.routesapp.data.StopwatchRepository
import com.example.routesapp.data.WorkoutsRepository
import com.example.shared.WorkoutSummary
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class StopwatchViewModel(
    val stopwatchRepository: StopwatchRepository,
    val routesRepository: RoutesRepository,
    val workoutsRepository: WorkoutsRepository,
    val authRepository: AuthRepository,
): ViewModel() {
    private val _elapsedTime = MutableStateFlow(Duration.ZERO)

    val elapsedTime = _elapsedTime.asStateFlow()
    val isRunning: StateFlow<Boolean> = stopwatchRepository.startMark.transform { start ->
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
            while (stopwatchRepository.startMark.value != null) {
                calcElapsed()
                delay(100)
            }
        }
    }
    fun calcElapsed() {
        val elapsed = stopwatchRepository.startMark.value?.elapsedNow() ?: Duration.ZERO
        _elapsedTime.value = stopwatchRepository.accumulatedTime.value + elapsed
    }
    fun start() {
        stopwatchRepository.start()
        launchElapsedCounter()
    }

    fun stop() = stopwatchRepository.stop()

    fun toggle()  {
        if(stopwatchRepository.startMark.value == null) {
            start()
        } else {
            stop()
        }
    }

    fun reset() {
        stopwatchRepository.reset()
        _elapsedTime.value = Duration.ZERO
    }

    @OptIn(ExperimentalTime::class)
    fun upload() {
        val duration = LocalTime(
            hour = elapsedTime.value.inWholeHours.toInt(),
            minute = (elapsedTime.value.inWholeMinutes % 60).toInt(),
            second = (elapsedTime.value.inWholeSeconds % 60).toInt(),
        )
        val timestamp = stopwatchRepository.startInstant.value
        val user = (authRepository.sessionState.value as? SessionState.LoggedIn)?.user
        val routeId = routesRepository.route.value?.summary?.id
        if (user != null && timestamp != null && routeId != null) {
            viewModelScope.launch {
                workoutsRepository.uploadWorkout(
                    WorkoutSummary(
                        user = user,
                        route = routeId,
                        timestamp = timestamp,
                        duration = duration,
                        private = false
                    )
                )
                reset()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                StopwatchViewModel(
                    (this[APPLICATION_KEY] as RoutesApp).appContainer.stopwatchRepository,
                    (this[APPLICATION_KEY] as RoutesApp).appContainer.routesRepository,
                    (this[APPLICATION_KEY] as RoutesApp).appContainer.workoutsRepository,
                    (this[APPLICATION_KEY] as RoutesApp).appContainer.authRepository,
                )
            }
        }
    }
}