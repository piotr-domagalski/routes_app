package com.example.routesapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.shared.WorkoutSummary
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun RouteHighscoresEntry(summary: WorkoutSummary, showRouteId: Boolean, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Text(summary.timestamp.toString())
        if (showRouteId) {
            Text("${summary.route}")
        }
        Text(summary.user)
        Text(summary.duration.toString())
    }
}