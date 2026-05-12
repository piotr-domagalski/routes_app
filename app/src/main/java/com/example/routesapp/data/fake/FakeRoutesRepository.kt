package com.example.routesapp.data.fake

import com.example.routesapp.data.RetrofitInstance
import com.example.routesapp.data.RoutesRepository

class FakeRoutesRepository : RoutesRepository() {
    private val api = RetrofitInstance.api

    override suspend fun getRoutes() = SampleRoutes.routes.map({route -> route.summary})

    override suspend fun getRoute(id: Int) =
        SampleRoutes.routes.find { route -> route.summary.id == id }
}