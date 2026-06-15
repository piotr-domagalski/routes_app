package com.example.routesapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.shared.WorkoutSummary

@Composable
fun RouteHighscoresList(workouts: List<WorkoutSummary>, showRouteId: Boolean, modifier: Modifier = Modifier) {
    if (workouts.isEmpty()) {
        Text("Brak wyników do wyświetlenia", modifier = modifier, textAlign = TextAlign.Center)
    } else {
        LazyColumn(modifier = modifier) {
            items(workouts) { workout ->
                RouteHighscoresEntry(workout, showRouteId, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}