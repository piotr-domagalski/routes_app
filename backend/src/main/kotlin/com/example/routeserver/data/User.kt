package com.example.routeserver.data

data class User(
    val username: String,
    val displayName: String?,
    val passwordHash: String
)