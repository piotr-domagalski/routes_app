package com.example.routesapp

class Route(id: Int, name: String, description: String, lengthKM: Float) {
    val id: Int = id
    val name: String = name
    val description: String = description
    val lengthKM: Float = lengthKM

    constructor(json: String) : this(0, "todo", "todo", 420.69F) {
        TODO()
    }
}

val exampleRoutes = listOf(
    Route(0, "Wartostrada Pętla", "pełna pętla", 15.67F),
    Route(1, "Wartostrada Asfalt", "odcinek przejeżdżalny asfaltem", 10.15F),
    Route(2, "Cytadela", "opis cytadeli", 5.2F),
    Route(3, "Kierskie", "pętla wokół jeziora kierskiego", 8.23F),
    Route(4, "Cytadela Sprint", "to jest test wyświetlania długości w metrach", 0.753F),
)