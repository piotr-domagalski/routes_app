package com.example.routesapp.data

import com.example.routesapp.data.RouteDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RoutesApi {

    @GET("routes")
    suspend fun getRoutes(): List<RouteDto>

    @GET("routes/{id}")
    suspend fun getRouteById(
        @Path("id") id: Int
    ): RouteDto
}