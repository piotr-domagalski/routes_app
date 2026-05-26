package com.example.routesapp.data.fake

import com.example.routesapp.data.RoutesApi
import com.example.routesapp.data.RoutesRepository
import com.example.shared.RouteDetails
import com.example.shared.RouteSummary


class FakeRoutesRepository() : RoutesRepository(FakeRoutesApi()) {
    override suspend fun getRoutes() = SampleRoutes.routes.map { route -> route.summary }

    override suspend fun getRoute(id: Int) =
        SampleRoutes.routes.find { route -> route.summary.id == id }

    class FakeRoutesApi: RoutesApi {
        override suspend fun getRoutes(): List<RouteSummary> { return emptyList() }
        override suspend fun getRouteById(id: Int): RouteDetails? { return null }
    }
}