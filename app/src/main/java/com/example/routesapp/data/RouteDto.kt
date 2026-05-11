package com.example.routesapp.data

import kotlinx.serialization.Serializable

@Serializable
class RouteDto(
    var id: Int,
    var name: String,
    var description: String,
    var lengthKM: Float
) {

    constructor(json: String) : this(0, "todo", "todo", 420.69F) {
        TODO()
    }
}

val exampleRoutes = listOf(
    RouteDto(0, "Wartostrada Pętla", "pełna pętla", 15.67F),
    RouteDto(1, "Wartostrada Asfalt", "odcinek przejeżdżalny asfaltem", 10.15F),
    RouteDto(2, "Cytadela", "opis cytadeli", 5.2F),
    RouteDto(3, "Kierskie", "pętla wokół jeziora kierskiego", 8.23F),
    RouteDto(4, "Cytadela Sprint", "to jest test wyświetlania długości w metrach", 0.753F),
)