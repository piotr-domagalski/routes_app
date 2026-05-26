package com.example.routesapp.data

import com.example.shared.WorkoutSummary
import com.example.shared.WorkoutsQuery

class WorkoutsRepository(val api: WorkoutsApi) {
    suspend fun getWorkouts(query: WorkoutsQuery): List<WorkoutSummary> {
        return api.getWorkouts(
            user = query.username,
            route = query.route,
            sort = query.sort,
            count = query.count,
            offset = query.offset
        )
    }

    suspend fun uploadWorkout(workout: WorkoutSummary) {
        return api.uploadWorkout(workout)
    }
}