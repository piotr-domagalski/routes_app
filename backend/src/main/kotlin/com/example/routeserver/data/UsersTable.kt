package com.example.routeserver.data

import org.jetbrains.exposed.v1.core.Table

object UsersTable : Table("users") {
    const val HASH_LENGTH = 256
    val username = varchar("username", length = 32)
    val displayName = varchar("display_name", length = 64).nullable()
    val passwordHash = varchar("password_hash", length = HASH_LENGTH)

    override val primaryKey = PrimaryKey(username)
}