package com.example.routesapp.data.fake

import com.example.shared.RouteDetails
import com.example.shared.RouteSummary

object SampleRoutes {
    val routes = listOf(
        RouteDetails(
            summary = RouteSummary(0, "Wartostrada Pętla", 15670),
            description = "pełna pętla"
        ),
        RouteDetails(
            summary = RouteSummary(1, "Wartostrada Asfalt", 10150),
            description = "odcinek przejeżdżalny asfaltem"
        ),
        RouteDetails(
            summary = RouteSummary(2, "Cytadela", 5200),
            description = "opis cytadeli"
        ),
        RouteDetails(
            summary = RouteSummary(3, "Kierskie", 8230),
            description = "pętla wokół jeziora kierskiego"
        ),
        RouteDetails(
            summary = RouteSummary(4, "Cytadela Sprint", 753),
            description = "to jest test wyświetlania długości w metrach"
        ),
        RouteDetails(
            summary = RouteSummary(5, "Puszczykowo i spowrotem", 4269),
            description = "ktor wita"
        ),
        RouteDetails(
            summary = RouteSummary(6, "Na Berlin", 288000),
            description = "reverse blitzkrieg"
        )
    )
    val routeDetails = routes[0]
    val routeSummary = routeDetails.summary
}