package com.example.routeserver

import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.jdbc.Database

fun Application.configureDatabaseConnection() {
    val dbUrl = System.getenv("DB_URL")
    val dbUser = System.getenv("DB_USER")
    val dbPassword = System.getenv("DB_PASSWORD")
    val mariadb = Database.connect(
        url = dbUrl,
        driver = "org.mariadb.jdbc.Driver",
        user = dbUser,
        password = dbPassword
    )
}
