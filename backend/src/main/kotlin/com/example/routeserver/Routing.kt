package com.example.routeserver

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

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
            call.respondText("Hello from Ktor!")
        }
        get("/routes") {
            call.respond(
                exampleRoutes
            )
        }
        get("/routes/{id}") {
            val routeId: Int? = call.parameters["id"]?.toInt()
            val route: Route? = exampleRoutes.find({route -> route.id == routeId})
            if (route != null) {
                call.respond(route)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}