package com.example.routeserver.data

import com.example.shared.LoginRequest
import com.example.shared.RegistrationRequest
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import java.security.SecureRandom
import kotlin.io.encoding.Base64
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

class AuthService(val usersRepository: UsersRepository, val sessionsRepository: SessionsRepository) {

    @OptIn(ExperimentalTime::class)
    fun validateToken(token: ByteArray): Session? {
        val decoded = ByteArray(SessionsTable.TOKEN_LENGTH)
        Base64.decodeIntoByteArray(token, decoded)
        val session = sessionsRepository.getSessionByToken(decoded)
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        if (session == null) {
            return null
        } else if (session.expires < now) {
            return null
        } else {
            val newExpires = newExpirationTime()
            sessionsRepository.updateExpires(decoded, newExpires)
            return session
        }
    }

    fun register(req: RegistrationRequest): Boolean {
        val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        val hash = encoder.encode(req.password) ?: return false
        if (null != usersRepository.getUserByUsername(req.username)) {
            return false
        } else {
            val user = User(req.username, req.displayName, hash)
            println("registering user: $user")
            usersRepository.insertUser(user)
            return true
        }
    }

    fun login(req: LoginRequest): Session? {
        val user = usersRepository.getUserByUsername(req.username) ?: return null
        println("login for user: $user")
        val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        return if (encoder.matches(req.password, user.passwordHash)) {
            createSession(req.username)
        } else {
            null
        }
    }

    fun logout(token: String): Boolean {
        return sessionsRepository.deleteSessionByToken(Base64.decode(token))
    }

    @OptIn(ExperimentalTime::class)
    private fun createSession(user: String): Session {
        val time = newExpirationTime()
        val rng = SecureRandom.getInstance("NativePRNG")
        val token = ByteArray(SessionsTable.TOKEN_LENGTH)
        rng.nextBytes(token)
        val session = Session(
            token,
            user,
            expires = time
        )
        sessionsRepository.insertSession(session)
        return session
    }

    @OptIn(ExperimentalTime::class)
    private fun newExpirationTime(): LocalDateTime {
        return (Clock.System.now() + 1.minutes).toLocalDateTime(TimeZone.currentSystemDefault())
    }

}