package com.example.routesapp.data

import com.example.shared.LoginRequest

open class AuthRepository(
    private val api: AuthApi,
    private val sessionManager: SessionManager
) {
    val sessionState = sessionManager.state
    suspend fun login(req: LoginRequest): Boolean {
        val resp = api.login(req)
        val token = resp.token
        if (token != null) {
            sessionManager.setState(token, req.username)
        }
        return token != null
    }
    suspend fun logout() {
        api.logout()
        sessionManager.clearState()
    }

    open suspend fun register() = api.register()

}