package com.example.routeserver.data

import com.example.shared.RouteDetails
import com.example.shared.RouteSummary
import com.example.shared.RoutesQuery
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class RoutesRepository {
    fun getRoutes(query: RoutesQuery?): List<RouteSummary> {
        val routeSummaries: List<RouteSummary> = transaction {
            val q = RoutesTable.select(RoutesTable.summaryColumns)

            val search = query?.search
            search?.let {
                q.andWhere {
                    (RoutesTable.name like "%${search}%") or
                    (RoutesTable.description like "%${search}%")
                }
            }

            val activityType = query?.activityType
            activityType?.let {
                q.andWhere { RoutesTable.activityType eq activityType }
            }

            val routeType = query?.routeType
            routeType?.let {
                q.andWhere { RoutesTable.routeType eq routeType }
            }

            q.offset(query?.offset ?: 0) // legacy support elvis consts
            q.limit(query?.count ?: 100)

            q.map {
                RouteSummary(
                    id = it[RoutesTable.id],
                    name = it[RoutesTable.name],
                    distanceMeters = it[RoutesTable.distanceMeters],
                    routeType = it[RoutesTable.routeType],
                    activityType = it[RoutesTable.activityType],
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
                            routeType = it[RoutesTable.routeType],
                            activityType = it[RoutesTable.activityType],
                            ),
                        description = it[RoutesTable.description],
                        thumbnailURI = null,
                        imageURI = null,
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

    fun insertRoute(route: RouteDetails) {
        transaction {
            addLogger(StdOutSqlLogger)
            RoutesTable.insert {
                it[RoutesTable.id] = route.summary.id
                it[RoutesTable.name] = route.summary.name
                it[RoutesTable.distanceMeters] = route.summary.distanceMeters
                it[RoutesTable.activityType] = route.summary.activityType
                it[RoutesTable.routeType] = route.summary.routeType
                it[RoutesTable.description] = route.description
                it[RoutesTable.thumbnailURI] = route.thumbnailURI
                it[RoutesTable.imageURI] = route.imageURI
            }
        }
    }

    fun getThumbnailURI(routeId: Int): String? {
        val uris: List<String?> = transaction {
            RoutesTable.select(RoutesTable.thumbnailURI)
                .where {RoutesTable.id eq routeId}
                .map { it[RoutesTable.thumbnailURI] }
        }

        check(uris.size <= 1) { "multiple routes with identical id (=$routeId) should not exist" }

        return uris.firstOrNull()
    }

    fun getImageURI(routeId: Int): String? {
        val uris: List<String?> = transaction {
            RoutesTable.select(RoutesTable.imageURI)
                .where {RoutesTable.id eq routeId}
                .map { it[RoutesTable.imageURI] }
        }

        check(uris.size <= 1) { "multiple routes with identical id (=$routeId) should not exist" }

        return uris.firstOrNull()
    }
}