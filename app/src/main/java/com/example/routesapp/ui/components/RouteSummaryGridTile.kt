package com.example.routesapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.routesapp.data.fake.SampleRoutes
import com.example.routesapp.ui.theme.RoutesAppTheme
import com.example.shared.RouteSummary

@Composable
fun RouteSummaryGridTile(route: RouteSummary, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            AsyncImage(model = "https://picsum.photos/seed/${route.id}/300",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(
            text = route.name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DistanceText(
                route.distanceMeters,
                modifier = Modifier.weight(1f)
            )
            HorizontalActivityTypeIcon(route.activityType)
            RouteTypeIcon(route.routeType, modifier = Modifier.size(32.dp))
        }
    }
}



@Composable
@Preview(showBackground = true)
fun RouteSummaryGridTilePreview() {
    RoutesAppTheme {
        RouteSummaryGridTile(SampleRoutes.routeSummary)
    }
}
