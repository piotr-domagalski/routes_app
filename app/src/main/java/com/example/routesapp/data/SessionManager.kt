package com.example.routesapp.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SessionState {
    data object Loading : SessionState
    data object LoggedOut : SessionState
    data class LoggedIn(val token: String, val user: String) : SessionState
}

class SessionManager(
    private val authStore: AuthStore,
    private val scope: CoroutineScope
) {
    val state: StateFlow<SessionState> = combine(authStore.tokenFlow, authStore.userFlow) {
            t, u ->
            if (t == null || u == null) { SessionState.LoggedOut }
            else { SessionState.LoggedIn(t, u) }
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = SessionState.Loading
        )

    fun setState(token: String, user: String) {
        scope.launch {
            authStore.save(token, user)
        }
    }

     fun clearState() {
        scope.launch {
            authStore.clear()
        }
    }
}
