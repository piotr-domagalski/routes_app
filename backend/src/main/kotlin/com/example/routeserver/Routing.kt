package com.example.routeserver

import com.example.routeserver.db.RoutesTable
import com.example.shared.RouteDetails
import com.example.shared.RouteSummary
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello from Ktor! This is a minor change to test BuildKit speedup.")
        }
        get("/db_test/all_tables") {
            transaction {
                val tables = exec(
                    """
                    SHOW TABLES;
                    """.trimIndent()
                ) { rs ->
                    buildList {
                        while (rs.next()) {
                            add(rs.getString(1))
                        }
                    }
                }

                println(tables)
            }
            call.respond(HttpStatusCode.OK)

        }
        get("/db_test/route_count") {
            val route_count = transaction {
                RoutesTable.selectAll().count()
            }
            call.respond(route_count)
        }

        get("/routes") {
            val routeSummaries: List<RouteSummary> = transaction {
                RoutesTable.selectAll().map {
                    RouteSummary(
                        id = it[RoutesTable.id],
                        name = it[RoutesTable.name],
                        distanceMeters = it[RoutesTable.distanceMeters],
                    )
                }
            }
            call.respond(routeSummaries)
        }

        get("/routes/{id}") {
            val routeId: Int? = call.parameters["id"]?.toIntOrNull()
            if (routeId == null) {
                call.respond(HttpStatusCode.BadRequest, "id not an int")
                return@get
            }
            val routes: List<RouteDetails> = transaction {
                RoutesTable
                    .selectAll()
                    .where { RoutesTable.id eq routeId }
                    .map {
                        RouteDetails(
                            summary = RouteSummary(
                                id = it[RoutesTable.id],
                                name = it[RoutesTable.name],
                                distanceMeters = it[RoutesTable.distanceMeters],
                            ),
                            description = it[RoutesTable.description],
                        )
                    }
            }

            if (routes.isEmpty()) {
                call.respond(HttpStatusCode.NotFound)
            } else if (routes.size == 1) {
                call.respond(routes.first())
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Multiple routes found for unique id")
            }
        }
    }
}