package com.example.routesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routesapp.data.RoutesRepository
import com.example.shared.RouteDetails
import com.example.shared.RouteSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoutesViewModel : ViewModel() {
    private val repo = RoutesRepository()
    private val _routeSummaries = MutableStateFlow(emptyList<RouteSummary>())
    private val _routeDetails = MutableStateFlow<RouteDetails?>(null)

    val routes = _routeSummaries.asStateFlow()
    val route = _routeDetails.asStateFlow()

    init {
        loadRoutes()
    }

    fun loadRoutes() {
        viewModelScope.launch {
            _routeSummaries.value = repo.getRoutes()
        }
    }

    fun getRouteById(id: Int) {
        viewModelScope.launch {
            _routeDetails.value = repo.getRoute(id)
        }
    }
}