package com.example.routesapp.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.routesapp.R
import com.example.routesapp.ui.theme.RoutesAppTheme
import com.example.shared.ActivityType

@Composable
fun HorizontalActivityTypeIcon(type: ActivityType?, modifier: Modifier = Modifier) {
    val colourOn = LocalContentColor.current
    val colourOff = LocalContentColor.current.copy(alpha = 0.25f)
    val bikeColour = when (type) {
        ActivityType.BIKE -> colourOn
        ActivityType.BOTH -> colourOn
        else -> colourOff
    }
    val shoeColour = when (type) {
        ActivityType.RUN -> colourOn
        ActivityType.BOTH -> colourOn
        else -> colourOff
    }
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.ic_bike),
            contentDescription = null,
            colorFilter = ColorFilter.tint(bikeColour),
            modifier = Modifier.size(32.dp)
        )
        Image(
            painter = painterResource(R.drawable.ic_shoe),
            contentDescription = null,
            colorFilter = ColorFilter.tint(shoeColour),
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun HorizontalActivityTypeIconPreview() {
    RoutesAppTheme {
        Column {
            HorizontalActivityTypeIcon(null)
            HorizontalActivityTypeIcon(ActivityType.RUN)
            HorizontalActivityTypeIcon(ActivityType.BIKE)
            HorizontalActivityTypeIcon(ActivityType.BOTH)
        }
    }
}