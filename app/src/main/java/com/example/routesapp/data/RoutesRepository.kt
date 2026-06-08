package com.example.routesapp.data

import com.example.shared.RouteDetails
import com.example.shared.RouteSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class RoutesRepository(
    private val api: RoutesApi
) {
    private val _routeSummaries = MutableStateFlow(emptyList<RouteSummary>())
    private val _routeDetails = MutableStateFlow<RouteDetails?>(null)
    private val _routeLookupError = MutableStateFlow(false)

    val routes = _routeSummaries.asStateFlow()
    val route = _routeDetails.asStateFlow()
    val routeLookupError = _routeLookupError.asStateFlow()

    open suspend fun loadRoutes() {
        _routeSummaries.value = api.getRoutes()
    }

    open suspend fun loadRouteById(id: Int) {
        _routeDetails.value = api.getRouteById(id)
        _routeLookupError.value = (_routeDetails.value == null)
    }

    fun forgetRoute() {
        _routeDetails.value = null
        _routeLookupError.value = false
    }

}