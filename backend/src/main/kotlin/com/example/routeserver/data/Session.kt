package com.example.routeserver.data

import kotlinx.datetime.LocalDateTime

data class Session(
    val token: ByteArray,
    val user: String,
    val expires: LocalDateTime
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Session

        if (!token.contentEquals(other.token)) return false
        if (user != other.user) return false
        if (expires != other.expires) return false

        return true
    }

    override fun hashCode(): Int {
        var result = token.contentHashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + expires.hashCode()
        return result
    }
}
