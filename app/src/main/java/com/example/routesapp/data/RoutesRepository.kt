package com.example.routesapp.data

open class RoutesRepository {
    private val api = RetrofitInstance.api

    open suspend fun getRoutes() = api.getRoutes()

    open suspend fun getRoute(id: Int) = api.getRouteById(id)
}