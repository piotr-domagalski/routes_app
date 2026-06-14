package com.example.routesapp.data.fake

import com.example.routesapp.data.RoutesApi
import com.example.routesapp.data.RoutesRepository
import com.example.shared.ActivityType
import com.example.shared.RouteDetails
import com.example.shared.RouteSummary
import com.example.shared.RouteType

class FakeRoutesRepository() : RoutesRepository(FakeRoutesApi()) {
    suspend fun getRoutes() = SampleRoutes.routes.map { route -> route.summary }

    suspend fun getRoute(id: Int) =
        SampleRoutes.routes.find { route -> route.summary.id == id }

    class FakeRoutesApi: RoutesApi {
        override suspend fun getRoutes(
            search: String?,
            activityType: ActivityType?,
            routeType: RouteType?,
            count: Int?,
            offset: Long?
        ): List<RouteSummary> { return emptyList() }
        override suspend fun getRouteById(id: Int): RouteDetails? { return null }
    }
}