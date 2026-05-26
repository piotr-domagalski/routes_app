package com.example.routeserver.data

import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class UsersRepository(val sessionsRepository: SessionsRepository) {
    fun getUserByUsername(username: String): User? {
        val users = transaction {
            UsersTable.selectAll().where(UsersTable.username eq username).map {
                User(
                    username = it[UsersTable.username],
                    displayName = it[UsersTable.displayName],
                    passwordHash = it[UsersTable.passwordHash]
                )
            }
        }
        return when (users.size) {
            0 -> null
            1 -> users.first()
            else -> error("Found multiple users with username $username")
        }
    }

    fun insertUser(user: User) {
        transaction {
            addLogger(StdOutSqlLogger)
            UsersTable.insert {
                it[UsersTable.username] = user.username
                it[UsersTable.displayName] = user.displayName
                it[UsersTable.passwordHash] = user.passwordHash
            }
        }
    }

    fun updateDisplayName(username: String, newDisplayName: String) {
        TODO()
    }
}