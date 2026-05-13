package com.example.routesapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.data.fake.SampleRoutes
import com.example.routesapp.ui.RoutesViewModel
import com.example.routesapp.ui.components.RouteSummaryHeader
import com.example.routesapp.ui.theme.RoutesAppTheme
import com.example.shared.RouteDetails

@Composable
fun RouteDetailsScreen(route: RouteDetails?, modifier: Modifier = Modifier) {
    if (route != null) {
        Column(modifier = modifier.padding(16.dp)) {
            RouteSummaryHeader(route.summary)
            Text(route.description)
        }
    } else {
         Text("Route not found")
    }
}

@Composable
@Preview(showBackground = true)
fun RouteDetailsScreenPreview() {
    RoutesAppTheme {
        RouteDetailsScreen(SampleRoutes.routeDetails, Modifier.fillMaxSize())
    }
}
