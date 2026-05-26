package com.example.routesapp.data

import com.example.shared.WorkoutSummary
import com.example.shared.WorkoutsQuery
import com.example.shared.WorkoutsQuerySortOrder
import io.ktor.http.HttpStatusCode
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import kotlin.text.toIntOrNull

interface WorkoutsApi {
    @GET("workouts")
    suspend fun getWorkouts(
        @Query("user") user: String? = null,
        @Query("route") route: Int? = null,
        @Query("sort") sort: WorkoutsQuerySortOrder = WorkoutsQuery.DEFAULT_SORT,
        @Query("count") count: Int = WorkoutsQuery.DEFAULT_COUNT,
        @Query("offset") offset: Long = WorkoutsQuery.DEFAULT_OFFSET,
        ): List<WorkoutSummary>

    @POST("workouts/upload")
    suspend fun uploadWorkout(@Body workoutSummary: WorkoutSummary)
}