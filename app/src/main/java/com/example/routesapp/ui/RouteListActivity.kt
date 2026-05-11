package com.example.routesapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.data.RouteDto
import com.example.routesapp.data.exampleRoutes
import com.example.routesapp.ui.theme.RoutesAppTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class RouteListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                RoutesAppTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                    ) { innerPadding ->
                        RouteList(
                            modifier = Modifier
                                .padding(innerPadding)
                                .consumeWindowInsets(innerPadding)
                        )

                    }
                }

       }
    }
}

@Composable
fun RouteList(viewModel: RoutesViewModel = viewModel(), modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val routes by viewModel.routes.collectAsState()
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(routes) { route ->
            RouteListItem(
                route,
                modifier = Modifier.clickable {
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
    }

}

@Composable
@Preview(showBackground = true)
fun RouteListPreview() {
    RoutesAppTheme {
        RouteList(viewModel())
    }
}

@Composable
fun RouteListItem(route: RouteDto, modifier: Modifier = Modifier) {
    Text(
        text = route.name,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            //.clickable {
            //    navController.navigate("details/$fruit")
            //}
    )
}

@Composable
@Preview(showBackground = true)
fun RouteListItemPreview() {
    RoutesAppTheme {
        RouteListItem(exampleRoutes[0])
    }
}
