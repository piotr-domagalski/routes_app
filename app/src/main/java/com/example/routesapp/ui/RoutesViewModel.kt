package com.example.routesapp.ui

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.routesapp.data.RouteDto
import com.example.routesapp.data.RoutesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoutesViewModel : ViewModel() {
    private val repo = RoutesRepository()
    private val _routes = MutableStateFlow(emptyList<RouteDto>())
    private val _route = MutableStateFlow<RouteDto?>(null)

    val routes = _routes.asStateFlow()
    val route = _route.asStateFlow()

    init {
        loadRoutes()
    }

    fun loadRoutes() {
        viewModelScope.launch {
            _routes.value = repo.getRoutes()
        }
    }

    fun getRouteById(id: Int) {
        viewModelScope.launch {
            _route.value = repo.getRoute(id)
        }
    }
}