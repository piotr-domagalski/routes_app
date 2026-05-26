package com.example.routeserver.routes

import com.example.routeserver.data.AuthService
import com.example.shared.LoginRequest
import com.example.shared.LoginResponse
import com.example.shared.RegistrationRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.authorization
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlin.io.encoding.Base64

fun Route.authRoutes(
    authService: AuthService
) {
    post("/register") {
        val req = call.receive<RegistrationRequest>()
        print("registration req: $req")
        val bool = authService.register(req)
        if (bool) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.Forbidden, "username already registered")
        }
    }

    post("/login") {
        val req = call.receive<LoginRequest>()
        print("login req: $req")
        val session = authService.login(req)
        if (session == null) {
            call.respond(HttpStatusCode.OK, LoginResponse(null))
        } else {
            call.respond(HttpStatusCode.Created, LoginResponse(Base64.encode(session.token)))
        }
    }

    post("/logout") {
        val auth = call.request.authorization() ?: return@post call.respond(HttpStatusCode.Unauthorized)
        print("logout req: $auth")
        if (authService.logout(auth)) {
            call.respond(HttpStatusCode.OK)
        } else {
            call.respond(HttpStatusCode.Unauthorized)
        }
    }
}