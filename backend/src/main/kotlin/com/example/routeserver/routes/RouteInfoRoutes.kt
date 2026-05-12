package com.example.routeserver

import com.example.routeserver.data.RoutesRepository
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
            val routeSummaries = routesRepository.getRoutes()
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