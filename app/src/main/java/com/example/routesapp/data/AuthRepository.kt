package com.example.routesapp.data

import com.example.shared.LoginRequest

open class AuthRepository(
    private val api: AuthApi
) {

    open suspend fun login(req: LoginRequest) = api.login(req)
    open suspend fun logout() = api.logout()
    open suspend fun register() = api.register()

}