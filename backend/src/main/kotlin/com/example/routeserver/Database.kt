package com.example.routeserver

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.core.DatabaseConfig
import org.jetbrains.exposed.v1.jdbc.Database

fun Application.configureDatabaseConnection() {
    val dbUrl = System.getenv("DB_URL")
    val dbUser = System.getenv("DB_USER")
    val dbPassword = System.getenv("DB_PASSWORD")

    val config = HikariConfig().apply {
        jdbcUrl = dbUrl
        driverClassName = "org.mariadb.jdbc.Driver"
        username = dbUser
        password = dbPassword
        maximumPoolSize = 6
        // as of version 0.46.0, if these options are set here, they do not need to be duplicated in DatabaseConfig
        isReadOnly = false
        transactionIsolation = "TRANSACTION_SERIALIZABLE"
    }

    val dataSource = HikariDataSource(config)

    val mariadb = Database.connect(
        datasource = dataSource,
        databaseConfig = DatabaseConfig { }
    )
}
