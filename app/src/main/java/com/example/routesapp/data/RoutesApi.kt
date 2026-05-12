package com.example.routesapp.data

import com.example.shared.RouteDetails
import com.example.shared.RouteSummary
import retrofit2.http.GET
import retrofit2.http.Path

interface RoutesApi {

    @GET("routes")
    suspend fun getRoutes(): List<RouteSummary>

    @GET("routes/{id}")
    suspend fun getRouteById(
        @Path("id") id: Int
    ): RouteDetails
}