package com.example.routeserver.data

import com.example.shared.RouteDetails
import com.example.shared.RouteSummary
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class RoutesRepository {
    fun getRoutes(): List<RouteSummary> {
        val routeSummaries: List<RouteSummary> = transaction {
            RoutesTable.selectAll().map {
                RouteSummary(
                    id = it[RoutesTable.id],
                    name = it[RoutesTable.name],
                    distanceMeters = it[RoutesTable.distanceMeters],
                )
            }
        }
        return routeSummaries
    }

    fun getRoute(id: Int): RouteDetails? {
        val routes: List<RouteDetails> = transaction {
            RoutesTable
                .selectAll()
                .where { RoutesTable.id eq id }
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

        return if (routes.isEmpty()) {
            null
        } else if (routes.size == 1) {
            routes.first()
        } else {
            error("Multiple rows found for id=$id")
        }
    }
}