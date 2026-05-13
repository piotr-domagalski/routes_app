package com.example.routesapp.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.data.fake.FakeRoutesRepository
import com.example.routesapp.ui.RouteDetailsActivity
import com.example.routesapp.ui.RoutesViewModel
import com.example.routesapp.ui.theme.RoutesAppTheme
import com.example.routesapp.ui.components.RouteSummaryHeader
@Composable
fun RouteListScreen(
    modifier: Modifier = Modifier,
    viewModel: RoutesViewModel = viewModel(),
    alternateRowColours: Boolean = false
) {
    val context = LocalContext.current
    val routes by viewModel.routes.collectAsState()
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(routes) { index, route ->
            val bg = if (!alternateRowColours) { MaterialTheme.colorScheme.surface }
                else if (index % 2 == 0) { MaterialTheme.colorScheme.surfaceContainer }
                else { MaterialTheme.colorScheme.surfaceContainerLow }
            Surface (
                color = bg
            ) {
                RouteSummaryHeader(
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
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview(showBackground = true)
fun RouteListScreenPreview() {
    val viewModel = RoutesViewModel(
        repository = FakeRoutesRepository()
    )

    RoutesAppTheme {
        RouteListScreen(viewModel = viewModel)
    }
}
