package com.example.routesapp.data.fake

import com.example.shared.ActivityType
import com.example.shared.RouteDetails
import com.example.shared.RouteSummary
import com.example.shared.RouteType

object SampleRoutes {
    val routes = listOf(
        RouteDetails(
            summary = RouteSummary(
                0,
                "Wartostrada Pętla",
                15670,
                RouteType.LOOP,
                ActivityType.BOTH),
            description = "pełna pętla",
            thumbnailURI = null
        ),
        RouteDetails(
            summary = RouteSummary(
                1,
                "Wartostrada Asfalt",
                10150,
                RouteType.ONEWAY,
                ActivityType.BOTH
            ),
            description = "odcinek przejeżdżalny asfaltem",
            thumbnailURI = null
        ),
        RouteDetails(
            summary = RouteSummary(
                2,
                "Cytadela",
                5200,
                RouteType.LOOP,
                ActivityType.BOTH
            ),
            description = "opis cytadeli",
            thumbnailURI = null
        ),
        RouteDetails(
            summary = RouteSummary(
                3,
                "Kierskie",
                8230,
                RouteType.LOOP,
                ActivityType.BOTH
            ),
            description = "pętla wokół jeziora kierskiego",
            thumbnailURI = null
        ),
        RouteDetails(
            summary = RouteSummary(
                4,
                "Cytadela Sprint",
                753,
                RouteType.ONEWAY,
                ActivityType.RUN
            ),
            description = "to jest test wyświetlania długości w metrach",
            thumbnailURI = null
        ),
        RouteDetails(
            summary = RouteSummary(
                5,
                "Puszczykowo i spowrotem",
                42690,
                RouteType.LOOP,
                ActivityType.BOTH
            ),
            description = "ktor wita",
            thumbnailURI = null
        ),
        RouteDetails(
            summary = RouteSummary(
                6,
                "Na Berlin",
                288000,
                RouteType.ONEWAY,
                ActivityType.BIKE
            ),
            description = "reverse blitzkrieg",
            thumbnailURI = null
        )
    )
    val routeDetails = routes[0]
    val routeSummary = routeDetails.summary
}
