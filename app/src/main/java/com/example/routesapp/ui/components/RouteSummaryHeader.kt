package com.example.routesapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.routesapp.data.fake.SampleRoutes
import com.example.routesapp.ui.theme.RoutesAppTheme
import com.example.shared.RouteSummary

@Composable
fun RouteSummaryHeader(route: RouteSummary, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = route.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = modifier
                    .padding(8.dp)
                //.clickable {
                //    navController.navigate("details/$fruit")
                //}
            )
            Text(
                text = "${route.distanceMeters / 1000F}km",
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier
                    .padding(8.dp)
                //.clickable {
                //    navController.navigate("details/$fruit")
                //}
            )
        }
        ActivityTypeIcon(route.activityType)
        RouteTypeIcon(route.routeType)
    }
}



@Composable
@Preview(showBackground = true)
fun RouteSummaryHeaderPreview() {
    RoutesAppTheme {
        RouteSummaryHeader(SampleRoutes.routeSummary)
    }
}
