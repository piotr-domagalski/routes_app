package com.example.routeserver.data

import com.example.shared.ActivityType
import com.example.shared.RegistrationRequest
import com.example.shared.RouteDetails
import com.example.shared.RouteSummary
import com.example.shared.RouteType
import com.example.shared.WorkoutSummary
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
fun seedDevData(authService: AuthService, workoutsRepository: WorkoutsRepository, routesRepository: RoutesRepository) {
    println("seeding with debug data...")
    seedDevRoutes(routesRepository)
    seedDevUsers(authService)
    seedDevWorkouts(workoutsRepository)
    println("\tseeding done.")
}

fun seedDevRoutes(routesRepository: RoutesRepository) {
    if (countRows(RoutesTable) > 0L) {
        println("\troutes table not empty, refusing.")
    } else {
        println("\tcreating routes...")

        for (route in SEED_ROUTES) {
            println("\tadding route: $route")
            routesRepository.insertRoute(route)
        }

        println("\tRoutes count: ${countRows(RoutesTable)}")
    }
}

fun seedDevUsers(authService: AuthService) {
    if (countRows(UsersTable) > 0L) {
        println("\tusers table not empty, refusing.")
    } else {
        println("\tcreating users...")

        for (req in SEED_REGISTRATION_REQUESTS) {
            authService.register(req)
        }

        println("Users count: ${countRows(UsersTable)}")
    }
}

fun seedDevWorkouts(workoutsRepository: WorkoutsRepository) {
    if (countRows(WorkoutsTable) > 0L) {
        println("\tworkouts table not empty, refusing.")
    } else {
        println("\tcreating workouts...")
        println("\t\t users is:")
        transaction {
            val users = UsersTable.selectAll().toList()
            println(users)

            val existsAlice = UsersTable
                .selectAll()
                .where { UsersTable.username eq "alice" }
                .count()

            println("\t\talice exists: $existsAlice")

            val routes = RoutesTable.selectAll().toList()
            println(routes)

            val existsRoute0 = RoutesTable
                .selectAll()
                .where { RoutesTable.id eq 0 }
                .count()

            println("\t\troute 0 exists: $existsRoute0")
        }

        for (workout in SEED_WORKOUTS) {
            println("\tadding workout: $workout")
            workoutsRepository.insertWorkout(
                workout
            )
        }

        println("\tWorkouts count: ${countRows(WorkoutsTable)}")
    }
}

val SEED_ROUTES: List<RouteDetails> = listOf(
    RouteDetails(
        RouteSummary(
            id = 1,
            name = "Wartostrada Pętla",
            distanceMeters = 15670,
            routeType = RouteType.LOOP,
            activityType = ActivityType.BOTH,
        ),
        description = "pełna pętla",
        thumbnailURI = "https://picsum.photos/seed/101/300",
        imageURI = "https://picsum.photos/seed/101/1920/1080"
    ),
    RouteDetails(
        RouteSummary(
            id = 2,
            name = "Wartostrada Asfalt",
            distanceMeters = 10150,
            routeType = RouteType.ONEWAY,
            activityType = ActivityType.BOTH,
        ),
        description = "odcinek przejeżdżalny asfaltem",
        thumbnailURI = "https://picsum.photos/seed/102/300",
        imageURI = "https://picsum.photos/seed/102/1920/1080"
    ),
    RouteDetails(
        RouteSummary(
            id = 3,
            name = "Cytadela",
            distanceMeters = 5200,
            routeType = RouteType.LOOP,
            activityType = ActivityType.BOTH,
        ),
        description = "opis cytadeli",
        thumbnailURI = "https://picsum.photos/seed/103/300",
        imageURI = "https://picsum.photos/seed/103/1920/1080"
    ),
    RouteDetails(
        RouteSummary(
            id = 4,
            name = "Kierskie",
            distanceMeters = 8230,
            routeType = RouteType.LOOP,
            activityType = ActivityType.BOTH,
        ),
        description = "pętla wokół jeziora kierskiego",
        thumbnailURI = "https://picsum.photos/seed/104/300",
        imageURI = "https://picsum.photos/seed/104/1920/1080"
    ),
    RouteDetails(
        RouteSummary(
            id = 5,
            name = "Cytadela Sprint",
            distanceMeters = 753,
            routeType = RouteType.ONEWAY,
            activityType = ActivityType.RUN,
        ),
        description = "to jest test wyświetlania długości w metrach",
        thumbnailURI = "https://picsum.photos/seed/105/300",
        imageURI = "https://picsum.photos/seed/105/1920/1080"
    ),
    RouteDetails(
        RouteSummary(
            id = 6,
            name = "Puszczykowo i z powrotem",
            distanceMeters = 42690,
            routeType = RouteType.LOOP,
            activityType = ActivityType.BOTH,
        ),
        description = "ktor wita",
        thumbnailURI = "https://picsum.photos/seed/106/300",
        imageURI = "https://picsum.photos/seed/106/1920/1080"
    ),
    RouteDetails(
        RouteSummary(
            id = 7,
            name = "Poznań - Piła",
            distanceMeters = 120000,
            routeType = RouteType.ONEWAY,
            activityType = ActivityType.BIKE,
        ),
        description = "chce ci się? fr?",
        thumbnailURI = "https://picsum.photos/seed/107/300",
        imageURI = "https://picsum.photos/seed/107/1920/1080"
    ),
    RouteDetails(
        RouteSummary(
            id = 8,
            name = "Las w wiosce",
            distanceMeters = 13370,
            routeType = RouteType.LOOP,
            activityType = ActivityType.BIKE,
        ),
        description = "idk dodaję żeby lista musiała się scrollować",
        thumbnailURI = "https://picsum.photos/seed/108/300",
        imageURI = "https://picsum.photos/seed/108/1920/1080"
    ),
    RouteDetails(
        RouteSummary(
            id = 9,
            name = "Trasa z długim opisem",
            distanceMeters = 69420,
            routeType = RouteType.LOOP,
            activityType = ActivityType.RUN,
        ),
        description = """
            |To jest trasa z bardzo długim opisem.
            |Jest długi ponieważ trasa ta jest bardzo ciekawa i wymaga długiego opisu by w pełni oddać jej ciekawość.
            |Gdyby opis był krótki to nie opisywałby odpowiednio szczegółowo tej trasy, dlatego jest długi.
            """.trimMargin(),
        thumbnailURI = "https://picsum.photos/seed/109/300",
        imageURI = "https://picsum.photos/seed/109/1920/1080"
    ),
    RouteDetails(
        RouteSummary(
            id = 10,
            name = "Kolejna trasa",
            distanceMeters = 28008,
            routeType = RouteType.LOOP,
            activityType = ActivityType.BIKE,
        ),
        description = "kończą mi się śmieszne liczby :'(",
        thumbnailURI = "https://picsum.photos/seed/110/300",
        imageURI = "https://picsum.photos/seed/110/1920/1080"
    ),
    RouteDetails(
        RouteSummary(
            id = 11,
            name = "A co gdyby",
            distanceMeters = 67,
            routeType = RouteType.LOOP,
            activityType = ActivityType.BIKE,
        ),
        description = "dwucyfrowy dystans",
        thumbnailURI = "https://picsum.photos/seed/111/300",
        imageURI = "https://picsum.photos/seed/111/1920/1080"
    ),
    RouteDetails(
        RouteSummary(
            id = 12,
            name = "Spacer do biedry",
            distanceMeters = 278,
            routeType = RouteType.ONEWAY,
            activityType = ActivityType.RUN,
        ),
        description = "po dwunastopak żubra",
        thumbnailURI = "file:///data/images/biedra.jpg",
        imageURI = "file:///data/images/biedra_inside.jpg"
    ),
    RouteDetails(
        RouteSummary(
            id = 13,
            name = "Spacer do kerfa",
            distanceMeters = 1476,
            routeType = RouteType.LOOP,
            activityType = ActivityType.RUN,
        ),
        description = "po prawdziwe piwo, i powrót bo nie jestem alkoholikiem i mogę dojść do domu o własnych siłach",
        thumbnailURI = "https://picsum.photos/seed/113/300",
        imageURI = "https://picsum.photos/seed/113/1920/1080"
    ),
    RouteDetails(
        RouteSummary(
            id = 14,
            name = "Do lodówki",
            distanceMeters = 3,
            routeType = RouteType.ONEWAY,
            activityType = ActivityType.RUN,
        ),
        description = "otworzyć kolejnego żubra",
        thumbnailURI = "https://picsum.photos/seed/114/300",
        imageURI = "https://picsum.photos/seed/114/1920/1080"
    ),
    RouteDetails(
        RouteSummary(
            id = 15,
            name = "Na Księżyc",
            distanceMeters = 384400000,
            routeType = RouteType.ONEWAY,
            activityType = ActivityType.RUN,
        ),
        description = "endurance run world record",
        thumbnailURI = "file:///data/images/FullMoon2010.jpg",
        imageURI = "file:///data/images/moonrun.jpg"
    )
)

val SEED_REGISTRATION_REQUESTS: List<RegistrationRequest> = listOf(
    RegistrationRequest(
        username = "alice",
        displayName = "Alice",
        password = "alice123"
    ),
    RegistrationRequest(
        username = "bob",
        displayName = "Bob",
        password = "bob123"
    )
)

@OptIn(ExperimentalTime::class)
val SEED_WORKOUTS: List<WorkoutSummary> = listOf(
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
