package com.example.routesapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.routesapp.R
import com.example.routesapp.ui.theme.RoutesAppTheme
import com.example.shared.RouteType

@Composable
fun RouteTypeIcon(type: RouteType) {
    val colourOn = MaterialTheme.colorScheme.onSurfaceVariant
    val colourOff = MaterialTheme.colorScheme.surfaceVariant
    val imageModifier = Modifier.size(48.dp)
    when (type) {
        RouteType.LOOP -> {
            Box {
                Image(
                    painter = painterResource(R.drawable.ic_oneway_grayout),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colourOff),
                    modifier = imageModifier
                )
                Image(
                    painter = painterResource(R.drawable.ic_loop),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colourOn),
                    modifier = imageModifier
                )
            }
        }
        RouteType.ONEWAY -> {
            Box {
                Image(
                    painter = painterResource(R.drawable.ic_loop_grayout),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colourOff),
                    modifier = imageModifier
                )
                Image(
                    painter = painterResource(R.drawable.ic_oneway),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colourOn),
                    modifier = imageModifier
                )
            }
        }
    }
}

@Composable
@Preview
fun RouteTypeIconPreview() {
    RoutesAppTheme {
        Row {
            RouteTypeIcon(RouteType.ONEWAY)
            RouteTypeIcon(RouteType.LOOP)
        }
    }
}

