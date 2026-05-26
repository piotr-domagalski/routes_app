package com.example.routesapp.data

import com.example.shared.LoginRequest
import com.example.shared.LoginResponse
import com.example.shared.RouteDetails
import com.example.shared.RouteSummary
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {

    @POST("login")
    suspend fun login(@Body req: LoginRequest): LoginResponse

    @POST("logout")
    suspend fun logout()

    @POST("register")
    suspend fun register()
}