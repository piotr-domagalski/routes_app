package com.example.routesapp.data

import com.example.shared.RouteDetails
import com.example.shared.RouteSummary
import com.example.shared.RoutesQuery
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

    var query = MutableStateFlow(RoutesQuery(count=10))

    open suspend fun loadRoutes() {
        val q = query.value.withBounds(offset = 0)
        query.value = q

        println("loading routes. query=${q}")

        _routeSummaries.value = api.getRoutes(
            q.search,
            q.activityType,
            q.routeType,
            q.count,
            q.offset
        )
    }

    open suspend fun loadMoreRoutes() {
        query.value = query.value.withBounds(
            offset = query.value.offset + query.value.count
        )
        val q = query.value

        println("loading more routes. query=${q}")

        _routeSummaries.value += api.getRoutes(
            q.search,
            q.activityType,
            q.routeType,
            q.count,
            q.offset
        )
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