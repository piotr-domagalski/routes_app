package com.example.routesapp.data

class RoutesRepository {
    private val api = RetrofitInstance.api

    suspend fun getRoutes() = api.getRoutes()

    suspend fun getRoute(id: Int) = api.getRouteById(id)
}