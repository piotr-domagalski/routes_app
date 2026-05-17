package com.example.routeserver.data

import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update

class SessionsRepository {
    fun getSessionByToken(token: ByteArray): Session? {
        val sessions = transaction {
            SessionsTable.selectAll().where(SessionsTable.token eq token).map({
                Session(
                    token = it[SessionsTable.token],
                    user = it[SessionsTable.user],
                    expires = it[SessionsTable.expires],
                )
            })
        }
        return when (sessions.size) {
            0 -> null
            1 -> sessions.first()
            else -> error("Found multiple sessions with token $token")
        }
    }

    fun insertSession(session: Session) {
        transaction {
            SessionsTable.insert {
                it[SessionsTable.token] = session.token
                it[SessionsTable.user] = session.user
                it[SessionsTable.expires] = session.expires
            }
        }
    }

    fun deleteSessionByToken(token: ByteArray): Boolean {
        return 0 < transaction {
            SessionsTable.deleteWhere { SessionsTable.token eq token }
        }
    }

    fun updateExpires(token: ByteArray, newExpires: LocalDateTime) {
        transaction {
            SessionsTable.update({SessionsTable.token eq token}) {
                it[SessionsTable.expires] = newExpires
            }
        }
    }
}