package com.example.routesapp.ui.viewmodels

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.routesapp.RoutesApp
import com.example.routesapp.data.AuthRepository
import com.example.shared.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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