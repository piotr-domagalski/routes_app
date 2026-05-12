package com.example.routeserver.data

import org.jetbrains.exposed.v1.core.Table

object RoutesTable : Table("routes") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 127)
    val description = varchar("description", 511)
    val distanceMeters = integer("distanceMeters")

    override val primaryKey = PrimaryKey(id)
}