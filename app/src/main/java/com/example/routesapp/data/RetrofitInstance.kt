package com.example.routesapp.data

import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

object RetrofitInstance {

    private const val BASE_URL = "http://192.168.1.2:8080/"
    //private const val BASE_URL = "http://routes.domagalski.it/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .build()

    val api: RoutesApi = retrofit.create(RoutesApi::class.java)
}