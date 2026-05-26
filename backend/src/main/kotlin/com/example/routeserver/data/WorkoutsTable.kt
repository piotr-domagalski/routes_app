package com.example.routeserver.data

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.datetime
import org.jetbrains.exposed.v1.datetime.time
import org.jetbrains.exposed.v1.datetime.timestamp
import kotlin.time.ExperimentalTime

object WorkoutsTable: Table("workouts") {
    val id = integer("id").autoIncrement()
    val user = reference("user", UsersTable.username,
        onDelete = ReferenceOption.CASCADE)
    val route = reference("route", RoutesTable.id,
        onDelete = ReferenceOption.CASCADE)
    @OptIn(ExperimentalTime::class)
    val timestamp = timestamp("timestamp")
    val duration = time("duration")
    val private = bool("private")

    override val primaryKey = PrimaryKey(id)
}