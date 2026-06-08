package com.example.routesapp.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.routesapp.RoutesApp
import com.example.routesapp.data.AuthRepository
import com.example.routesapp.data.RoutesRepository
import com.example.routesapp.data.SessionManager
import com.example.shared.LoginRequest
import com.example.shared.RouteSummary
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource

class AuthViewModel(
    private val repository: AuthRepository,
): ViewModel() {
    val usernameFieldState: TextFieldState = TextFieldState()
    val passwordFieldState: TextFieldState = TextFieldState()
    private val _rememberMe: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _loginSuccessful: MutableStateFlow<Boolean?> = MutableStateFlow<Boolean?>(null)

    val sessionState = repository.sessionState
    val rememberMe = _rememberMe.asStateFlow()
    val loginSuccessful = _loginSuccessful.asStateFlow()

    fun setRememberMe(value: Boolean) {
        _rememberMe.value = value
    }

    fun attemptLogin() {
        viewModelScope.launch {
            _loginSuccessful.value = repository.login(
                    LoginRequest(
                        usernameFieldState.text.toString(),
                        passwordFieldState.text.toString()
                    )
                )
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _loginSuccessful.value = null
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AuthViewModel(
                    (this[APPLICATION_KEY] as RoutesApp).appContainer.authRepository,
                )
            }
        }
    }
}