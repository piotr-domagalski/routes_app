package com.example.routesapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.preview.SampleRoutes
import com.example.routesapp.ui.theme.RoutesAppTheme
import com.example.shared.RouteDetails

class RouteDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val routeId = this.intent.getIntExtra("route_id", -1)
        enableEdgeToEdge()
        setContent {
            RoutesAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    val routesViewModel: RoutesViewModel = viewModel()
                    routesViewModel.getRouteById(routeId)
                    val route = routesViewModel.route.collectAsState().value
                    if (route != null) {
                        RouteDetailsScreen(
                            route,
                            modifier = Modifier
                                .padding(innerPadding)
                                .consumeWindowInsets(innerPadding)
                        )
                    } else {
                        Text("Route with id $routeId not found")
                    }
                }
            }

        }
    }
}

@Composable
fun RouteDetailsScreen(route: RouteDetails, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                route.summary.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Text("${route.summary.distanceMeters/1000F}km")
        }
        Text(route.description)
    }
}

@Composable
@Preview(showBackground = true)
fun RouteDetailsScreenPreview() {
    RoutesAppTheme {
        RouteDetailsScreen(SampleRoutes.routeDetails, Modifier.fillMaxSize())
    }
}
