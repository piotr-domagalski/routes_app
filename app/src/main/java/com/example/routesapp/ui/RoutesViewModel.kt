package com.example.routesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.routesapp.RoutesApp
import com.example.routesapp.data.RoutesRepository
import kotlinx.coroutines.launch

class RoutesViewModel(
    private val repository: RoutesRepository
) : ViewModel() {

    val routes = repository.routes
    val route = repository.route
    val routeLookupError = repository.routeLookupError

    init {
        if (repository.routes.value.isEmpty()) {
            loadRoutes()
        }
    }

    fun loadRoutes() {
        viewModelScope.launch {
            repository.loadRoutes()
        }
    }

    fun loadMoreRoutes() {
        viewModelScope.launch {
            repository.loadMoreRoutes()
        }
    }

    fun getRouteById(id: Int) {
        viewModelScope.launch {
            repository.loadRouteById(id)
        }
    }

    fun forgetRoute() {
        repository.forgetRoute()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                RoutesViewModel(
                    (this[APPLICATION_KEY] as RoutesApp).appContainer.routesRepository
                )
            }
        }
    }

}