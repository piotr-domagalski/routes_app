package com.example.routesapp.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SessionState {
    data object Loading : SessionState
    data object LoggedOut : SessionState
    data class LoggedIn(val token: String) : SessionState
}

class SessionManager(
    private val authStore: AuthStore,
    private val scope: CoroutineScope
) {
    private var _token = MutableStateFlow<String?>(null)

    init {
        scope.launch {
            authStore.tokenFlow.collect { _token.value = it }
        }
    }
    val state: StateFlow<SessionState> = _token
            .map {
                if (it == null) SessionState.LoggedOut
                else SessionState.LoggedIn(it)
            }
            .stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = SessionState.Loading
            )

    fun setToken(token: String) {
        _token.value = token
        scope.launch {
            authStore.saveToken(token)
        }
    }

     fun clearToken() {
        _token.value = null
        scope.launch {
            authStore.clearToken()
        }
    }
}
