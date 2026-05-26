package com.example.routesapp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface SessionState {
    data object Loading : SessionState
    data object LoggedOut : SessionState
    data class LoggedIn(val token: String) : SessionState
}

class SessionManager(val authStore: AuthStore) {
    private val _state =
        MutableStateFlow<SessionState>(SessionState.Loading)

    val state = _state.asStateFlow()

    suspend fun initialize() {
        authStore.tokenFlow.collect { token ->
            _state.value =
                if (token == null) { SessionState.LoggedOut }
                else { SessionState.LoggedIn(token) }
        }
    }
}
