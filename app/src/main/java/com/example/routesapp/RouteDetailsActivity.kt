package com.example.routesapp

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.routesapp.ui.theme.RoutesAppTheme

class RouteDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val routeId = this.intent.getIntExtra("route_id", -1)
        val route: Route? = exampleRoutes.find({ route: Route -> route.id == routeId })
        enableEdgeToEdge()
        setContent {
            RoutesAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    if (route != null) {
                        RouteDetails(
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
fun RouteDetails(route: Route, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                route.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Text("${route.lengthKM}km")
        }
        Text(route.description)
    }
}

@Composable
@Preview(showBackground = true)
fun RouteDetailsPreview() {
    RoutesAppTheme {
        RouteDetails(exampleRoutes[0], Modifier.fillMaxSize())
    }
}
