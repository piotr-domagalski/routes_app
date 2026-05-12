package com.example.routeserver

import com.example.routeserver.db.RoutesTable
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

@Serializable
class Route(
    var id: Int,
    var name: String,
    var description: String,
    var lengthKM: Float
) {

    constructor(json: String) : this(0, "todo", "todo", 420.69F) {
        TODO()
    }
}
val exampleRoutes = listOf(
    Route(0, "Wartostrada Pętla", "pełna pętla", 15.67F),
    Route(1, "Wartostrada Asfalt", "odcinek przejeżdżalny asfaltem", 10.15F),
    Route(2, "Cytadela", "opis cytadeli", 5.2F),
    Route(3, "Kierskie", "pętla wokół jeziora kierskiego", 8.23F),
    Route(4, "Cytadela Sprint", "to jest test wyświetlania długości w metrach", 0.753F),
    Route(5, "Puszczykowo i spowrotem", "ktor wita", 42.69F),
)

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
            val routes: List<Route> = transaction {
                RoutesTable.selectAll().map {
                    Route(
                        id = it[RoutesTable.id],
                        name = it[RoutesTable.name],
                        description = it[RoutesTable.description],
                        lengthKM = it[RoutesTable.lengthM]/1000F,
                    )
                }
            }
            call.respond(routes)
        }
        get("/routes/{id}") {
            val routeId: Int? = call.parameters["id"]?.toIntOrNull()
            if (routeId == null) {
                call.respond(HttpStatusCode.BadRequest, "id not an int")
                return@get
            }
            val routes: List<Route> = transaction {
                RoutesTable
                    .selectAll()
                    .where { RoutesTable.id eq routeId }
                    .map {
                        Route(
                            id = it[RoutesTable.id],
                            name = it[RoutesTable.name],
                            description = it[RoutesTable.description],
                            lengthKM = it[RoutesTable.lengthM] / 1000F,
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