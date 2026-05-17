package com.example.routeserver.data

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.datetime
object SessionsTable : Table("sessions") {
    const val TOKEN_LENGTH=64
    val token = binary("token", length = TOKEN_LENGTH)
    val user = reference("user", UsersTable.username)
    val expires = datetime("expires")
    override val primaryKey = PrimaryKey(token)
}