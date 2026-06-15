package com.example.routeserver.data

import com.example.shared.ActivityType
import com.example.shared.RouteType
import org.jetbrains.exposed.v1.core.Table

object RoutesTable : Table("routes") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 127)
    val description = varchar("description", 511)
    val distanceMeters = integer("distanceMeters")
    val routeType = enumerationByName("type", 10, RouteType::class)
    val activityType = enumerationByName("activity", 10, ActivityType::class)

    val thumbnailURI = varchar("thumbnailURI", 255).nullable()
    val imageURI = varchar("imageURI", 255).nullable()

    override val primaryKey = PrimaryKey(id)

    val summaryColumns = listOf(id, name, distanceMeters, routeType, activityType)
}