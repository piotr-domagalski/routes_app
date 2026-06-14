package com.example.routesapp.data

import com.example.shared.ActivityType
import com.example.shared.RouteDetails
import com.example.shared.RouteSummary
import com.example.shared.RouteType
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RoutesApi {

    @GET("routes")
    suspend fun getRoutes(
        @Query("search_string") search: String? = null,
        @Query("activity_type") activityType: ActivityType? = null,
        @Query("route_type") routeType: RouteType? = null,
        @Query("count") count: Int? = null,
        @Query("offset") offset: Long? = null
    ): List<RouteSummary>

    @GET("routes/{id}")
    suspend fun getRouteById(
        @Path("id") id: Int
    ): RouteDetails?
}
