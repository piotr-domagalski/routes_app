package com.example.routeserver

import com.example.routeserver.data.RoutesRepository
import com.example.shared.ActivityType
import com.example.shared.RouteType
import com.example.shared.RoutesQuery
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import java.io.File
import java.net.URI
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

        suspend fun ApplicationCall.respondUri(uri: String) {
            if (uri.startsWith("file://")) {
                val file = File(URI(uri))

                if (!file.exists()) {
                    respond(
                        HttpStatusCode.InternalServerError,
                        "Thumbnail '$uri' missing"
                    )
                }

                respondFile(file)
            } else {
                respondRedirect(uri)
            }
        }

        get("/{id}/thumbnail") {
            val routeId: Int = call.parameters["id"]
                ?.toIntOrNull()
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "id must be an integer, got: ${call.parameters["id"]}"
            )

            val uri = routesRepository.getThumbnailURI(routeId)
                ?: return@get call.respond(
                        HttpStatusCode.NotFound,
                        "Route '$routeId' not found"
                    )

            //TODO: make this debug build only
            call.response.header(
                HttpHeaders.CacheControl,
                "no-cache"
            )

            call.respondUri(uri)
        }

        get("/{id}/image") {
            val routeId: Int = call.parameters["id"]
                ?.toIntOrNull()
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "id must be an integer, got: ${call.parameters["id"]}"
                )

            val uri = routesRepository.getImageURI(routeId)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    "Route '$routeId' not found"
                )

            //TODO: make this debug build only
            call.response.header(
                HttpHeaders.CacheControl,
                "no-cache"
            )

            call.respondUri(uri)
        }
    }
}