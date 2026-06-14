package com.example.routesapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.routesapp.ui.theme.RoutesAppTheme
import com.example.routesapp.ui.screens.RouteListScreen

class RouteListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            RoutesAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    RouteListScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding),
                        alternatingRowColours = true,
                        onClick = { route ->
                            val intent = Intent(
                            context,
                            RouteDetailsActivity::class.java
                        ).apply {
                            putExtra("route_id", route.id)
                        }
                            context.startActivity(intent)
                        },
                    )
                }
            }
        }
    }
}