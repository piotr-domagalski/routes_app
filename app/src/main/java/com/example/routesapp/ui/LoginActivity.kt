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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.RoutesApp
import com.example.routesapp.data.SessionState
import com.example.routesapp.ui.screens.LoginScreen
import com.example.routesapp.ui.screens.RouteDetailsScreen
import com.example.routesapp.ui.screens.RouteListScreen
import com.example.routesapp.ui.theme.RoutesAppTheme


class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as RoutesApp
        if (app.appContainer.sessionManager.state.value is SessionState.LoggedIn) {
            finish()
        }
        setContent {
            RoutesAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    LoginScreen(
                        onSuccess = { finish() },
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(32.dp)
                            .consumeWindowInsets(innerPadding)
                    )
                }
            }
        }
    }
}