package com.example.routeserver

import com.example.routeserver.data.RoutesRepository
import com.example.shared.ActivityType
import com.example.shared.RouteType
import com.example.shared.RoutesQuery
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlin.text.toIntOrNull

fun Route.routeInfoRoutes(
    routesRepository: RoutesRepository
) {
    route("/routes"){
        get {
            val searchString = call.request.queryParameters["search_string"]
            val activityTypeString = call.request.queryParameters["activity_type"]
            val routeTypeString = call.request.queryParameters["route_type"]

            val countString = call.request.queryParameters["count"]
            val offsetString = call.request.queryParameters["offset"]

            val activityType = when (activityTypeString) {
                ActivityType.BIKE.toString() -> ActivityType.BIKE
                ActivityType.RUN.toString() -> ActivityType.RUN
                ActivityType.BOTH.toString() -> ActivityType.BOTH
                null -> RoutesQuery.DEFAULT_ACTIVITY_TYPE
                else -> return@get call.respond(HttpStatusCode.BadRequest, "invalid activity type")
            }

            val routeType = when (routeTypeString) {
                RouteType.ONEWAY.toString() -> RouteType.ONEWAY
                RouteType.LOOP.toString() -> RouteType.LOOP
                null -> RoutesQuery.DEFAULT_ROUTE_TYPE
                else -> return@get call.respond(HttpStatusCode.BadRequest, "invalid route type")
            }

            val count = countString?.toIntOrNull()
            if (countString != null && count == null) {
                return@get call.respond(HttpStatusCode.BadRequest, "invalid count")
            }
            val offset = offsetString?.toLongOrNull()
            if (offsetString != null && offset == null) {
                return@get call.respond(HttpStatusCode.BadRequest, "invalid offset")
            }

            val query = RoutesQuery (
                searchString,
                activityType,
                routeType,
                count ?: RoutesQuery.DEFAULT_COUNT,
                offset ?: RoutesQuery.DEFAULT_OFFSET
            )

            val routeSummaries = routesRepository.getRoutes(query)
            call.respond(routeSummaries)
        }

        get("/{id}") {
            val routeId: Int = call.parameters["id"]
                ?.toIntOrNull()
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "id must be an integer, got: ${call.parameters["id"]}"
                )

            val route = routesRepository.getRoute(routeId)
            if (route != null) {
                call.respond(route)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}