package com.example.routeserver.routes

import com.example.routeserver.data.AuthService
import com.example.routeserver.data.WorkoutsRepository
import com.example.shared.WorkoutSummary
import com.example.shared.WorkoutsQuery
import com.example.shared.WorkoutsQuerySortOrder
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.authorization
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.workoutRoutes(
    authService: AuthService,
    workoutsRepository: WorkoutsRepository
) {
    route("/workouts") {
        post("/upload") {
            val workout = call.receive<WorkoutSummary>()
            val token = call.request.authorization() ?:
                return@post call.respond(HttpStatusCode.Unauthorized)
            val session = authService.validateToken(token.toByteArray()) ?:
                return@post call.respond(HttpStatusCode.Unauthorized)
            if (workout.user != session.user) {
                return@post call.respond(HttpStatusCode.Unauthorized)
            }
            workoutsRepository.insertWorkout(workout)
        }
        post("/remove") {
            call.respond(HttpStatusCode.ServiceUnavailable)
        }
        get {
            val token = call.request.authorization() ?:
            return@get call.respond(HttpStatusCode.Unauthorized)
            val session = authService.validateToken(token.toByteArray()) ?:
            return@get call.respond(HttpStatusCode.Unauthorized)

            val username = call.request.queryParameters["user"]
            val routeIdString = call.request.queryParameters["route"]
            val sortString = call.request.queryParameters["sort"]
            val countString = call.request.queryParameters["count"]
            val offsetString = call.request.queryParameters["offset"]

            val routeId = routeIdString?.toIntOrNull()
            if (routeIdString != null && routeId == null) {
                return@get call.respond(HttpStatusCode.BadRequest, "invalid route id")
            }

            val sort = when (sortString) {
                "recent" -> WorkoutsQuerySortOrder.RECENT
                "fastest" -> WorkoutsQuerySortOrder.FASTEST
                null -> WorkoutsQuery.DEFAULT_SORT
                else -> return@get call.respond(HttpStatusCode.BadRequest, "invalid sort")
            }

            val count = countString?.toIntOrNull()
            if (countString != null && count == null) {
                return@get call.respond(HttpStatusCode.BadRequest, "invalid count")
            }
            val offset = offsetString?.toLongOrNull()
            if (offsetString != null && offset == null) {
                return@get call.respond(HttpStatusCode.BadRequest, "invalid offset")
            }

            val query = WorkoutsQuery(
                username = username,
                route = routeId,
                sort = sort,
                count = count ?: WorkoutsQuery.DEFAULT_COUNT,
                offset = offset ?: WorkoutsQuery.DEFAULT_OFFSET,
            )

            call.respond(workoutsRepository.getWorkouts(query, session))
        }
    }
}