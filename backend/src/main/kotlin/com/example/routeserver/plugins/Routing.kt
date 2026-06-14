package com.example.routeserver.plugins

import com.example.routeserver.data.AuthService
import com.example.routeserver.routes.authRoutes
import com.example.routeserver.data.RoutesRepository
import com.example.routeserver.data.RoutesTable
import com.example.routeserver.data.SessionsRepository
import com.example.routeserver.data.UsersRepository
import com.example.routeserver.data.UsersTable
import com.example.routeserver.data.WorkoutsRepository
import com.example.routeserver.data.seedDevData
import com.example.routeserver.routeInfoRoutes
import com.example.routeserver.routes.workoutRoutes
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Application.configureRouting() {
    val routesRepository = RoutesRepository()
    val sessionsRepository = SessionsRepository()
    val usersRepository = UsersRepository(sessionsRepository)
    val workoutsRepository = WorkoutsRepository()
    val authService = AuthService(usersRepository, sessionsRepository)

    seedDevData(authService, workoutsRepository, routesRepository)

    routing {
        routeInfoRoutes(routesRepository)
        authRoutes(authService)
        workoutRoutes(authService, workoutsRepository)

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
            val routeCount = transaction {
                RoutesTable.selectAll().count()
            }
            call.respond(routeCount)
        }

    }
}