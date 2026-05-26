package com.example.routesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.routesapp.RoutesApp
import com.example.routesapp.data.RoutesRepository
import com.example.shared.RouteDetails
import com.example.shared.RouteSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoutesViewModel(
    private val repository: RoutesRepository
) : ViewModel() {
    private val _routeSummaries = MutableStateFlow(emptyList<RouteSummary>())
    private val _routeDetails = MutableStateFlow<RouteDetails?>(null)
    private val _routeLookupError = MutableStateFlow<Boolean>(false)

    val routes = _routeSummaries.asStateFlow()
    val route = _routeDetails.asStateFlow()
    val routeLookupError = _routeLookupError.asStateFlow()

    init {
        loadRoutes()
    }

    fun loadRoutes() {
        viewModelScope.launch {
            _routeSummaries.value = repository.getRoutes()
        }
    }

    fun getRouteById(id: Int) {
        viewModelScope.launch {
            _routeDetails.value = repository.getRoute(id)
            _routeLookupError.value = (_routeDetails.value == null)
        }
    }

    fun forgetRoute() {
        viewModelScope.launch {
            _routeDetails.value = null
            _routeLookupError.value = false
        }
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