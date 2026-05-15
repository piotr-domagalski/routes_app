package com.example.routesapp.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.data.fake.FakeRoutesRepository
import com.example.routesapp.ui.RouteDetailsActivity
import com.example.routesapp.ui.RoutesViewModel
import com.example.routesapp.ui.theme.RoutesAppTheme
import com.example.routesapp.ui.components.RouteSummaryHeader
import com.example.shared.RouteSummary

@Composable
fun RouteListScreen(
    modifier: Modifier = Modifier,
    viewModel: RoutesViewModel = viewModel(),
    alternateRowColours: Boolean = false,
    onClick: ((RouteSummary) -> Unit)? = null,
    highlightCallback: ((RouteSummary) -> Color?)? = null
) {
    val routes = viewModel.routes.collectAsState().value
    val selectedRoute = viewModel.route.collectAsState().value
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(routes) { index, route ->
            val bg = highlightCallback?.invoke(route)?:
                if (!alternateRowColours) { MaterialTheme.colorScheme.surface }
                else if (index % 2 == 0) { MaterialTheme.colorScheme.surfaceContainer }
                else { MaterialTheme.colorScheme.surfaceContainerLow }
            Surface (
                color = bg
            ) {
                RouteSummaryHeader(
                    route,
                    modifier = Modifier.clickable {
                        onClick?.invoke(route)
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
