package com.example.routeserver.data

import com.example.shared.RegistrationRequest
import com.example.shared.WorkoutSummary
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalTime
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun countRows(table: Table): Long {
    return transaction {
        return@transaction table.selectAll().count()
    }
}
@OptIn(ExperimentalTime::class)
fun seedDevData(authService: AuthService, workoutsRepository: WorkoutsRepository) {
    println("seeding with debug data...")
    if (countRows(UsersTable) > 0L) {
        println("\tusers table not empty, refusing.")
    } else {
        println("\tcreating users...")
        authService.register(
            RegistrationRequest(
                username = "alice",
                password = "alice123"
            ))
        authService.register(
            RegistrationRequest(
                username = "bob",
                password = "bob123"
            ))

        println("Users count: ${countRows(UsersTable)}")
    }

    if (countRows(WorkoutsTable) > 0L) {
        println("\tworkouts table not empty, refusing.")
    } else {
        println("\tcreating workouts...")
        println("\t\t users is:")
        transaction {
            val users = UsersTable.selectAll().toList()
            println(users)

            val exists_alice = UsersTable
                .selectAll()
                .where { UsersTable.username eq "alice" }
                .count()

            println("\t\talice exists: $exists_alice")

            val routes = RoutesTable.selectAll().toList()
            println(routes)

            val exists_0 = RoutesTable
                .selectAll()
                .where { RoutesTable.id eq 0 }
                .count()

            println("\t\troute 0 exists: $exists_0")
        }


        val workouts = listOf(
            WorkoutSummary(
                user = "alice",
                route = 1,
                timestamp = Instant.parse("2026-04-10T16:00:00Z"),
                duration = LocalTime.parse("01:10:00"),
                private = false
            ),
            WorkoutSummary(
                user = "alice",
                route = 2,
                timestamp = Instant.parse("2026-04-15T16:00:00Z"),
                duration = LocalTime.parse("00:30:00"),
                private = false
            ),
            WorkoutSummary(
                user = "alice",
                route = 2,
                timestamp = Instant.parse("2026-04-20T16:00:00Z"),
                duration = LocalTime.parse("00:25:00"),
                private = true
            ),
            WorkoutSummary(
                user = "alice",
                route = 3,
                timestamp = Instant.parse("2026-04-25T16:00:00Z"),
                duration = LocalTime.parse("02:15:00"),
                private = true
            ),
            WorkoutSummary(
                user = "bob",
                route = 1,
                timestamp = Instant.parse("2026-04-12T16:00:00Z"),
                duration = LocalTime.parse("01:23:00"),
                private = false
            ),
            WorkoutSummary(
                user = "bob",
                route = 2,
                timestamp = Instant.parse("2026-04-17T16:00:00Z"),
                duration = LocalTime.parse("00:18:00"),
                private = false
            ),
            WorkoutSummary(
                user = "bob",
                route = 2,
                timestamp = Instant.parse("2026-04-22T16:00:00Z"),
                duration = LocalTime.parse("00:35:00"),
                private = true
            ),
            WorkoutSummary(
                user = "bob",
                route = 3,
                timestamp = Instant.parse("2026-04-27T16:00:00Z"),
                duration = LocalTime.parse("01:45:00"),
                private = false
            )
        )

        for (workout in workouts) {
            println("\tadding workout: $workout")
            workoutsRepository.insertWorkout(
                workout
            )
        }
        println("\tWorkouts count: ${countRows(WorkoutsTable)}")
    }

    println("\tseeding done.")
}
