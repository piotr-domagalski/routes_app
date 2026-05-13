package com.example.routesapp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DistanceText(distanceMeters: Int, modifier: Modifier = Modifier) {
    val text = if (distanceMeters < 10000) { "${distanceMeters}m" }
        else { "${distanceMeters / 1000F}km" }
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )

}
