package com.example.routesapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.ui.screens.RouteDetailsScreen
import com.example.routesapp.ui.screens.RouteListScreen
import com.example.routesapp.ui.theme.RoutesAppTheme


class RouteCombinedActivity : ComponentActivity() {
    val combinedListWidthDp = 320.dp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoutesAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    RouteListAdaptive(
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    )
                }
            }
        }
    }

    @Composable
    fun RouteListAdaptive(modifier: Modifier = Modifier) {
        val displayMetrics: DisplayMetrics = LocalResources.current.displayMetrics
        val dpWidth = (displayMetrics.widthPixels / displayMetrics.density).dp

        if (dpWidth >= combinedListWidthDp*2) {
            RouteListAndDetails(modifier = modifier)
        } else {
            RouteListOnly(modifier = modifier)
        }
    }

    @Preview
    @Composable
    fun RouteListAdaptivePreview() {
        RouteListAdaptive(modifier = Modifier.fillMaxSize())
    }
    @Composable
    fun RouteListOnly(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        RouteListScreen(
            modifier = modifier,
            alternateRowColours = true,
            onClick = { route ->
                val intent = Intent(
                    context,
                    RouteDetailsActivity::class.java
                ).apply {
                    putExtra("route_id", route.id)
                }
                context.startActivity(intent)
            }
        )
    }

    @Composable
    fun RouteListAndDetails(modifier: Modifier = Modifier) {
        val routesViewModel: RoutesViewModel = viewModel()
        Row(
            modifier = modifier
        ) {
            RouteListScreen(
                modifier = Modifier.width(combinedListWidthDp),
                alternateRowColours = true,
                onClick = { route ->
                    if (routesViewModel.route.value?.summary?.id == route.id) {
                        routesViewModel.forgetRoute()
                    } else {
                        routesViewModel.getRouteById(route.id)
                    }
                }
            )
            val route = routesViewModel.route.collectAsState().value
            RouteDetailsScreen(route)
        }
    }
}