package com.example.routesapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shared.WorkoutSummary

@Composable
fun RouteHighscoresList(workouts: List<WorkoutSummary>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(workouts) { workout ->
            RouteHighscoresEntry(workout, modifier = Modifier.fillMaxWidth())
        }
    }
}