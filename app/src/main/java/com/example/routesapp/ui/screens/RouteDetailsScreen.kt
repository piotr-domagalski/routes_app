package com.example.routesapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.data.fake.FakeRoutesRepository
import com.example.routesapp.data.fake.SampleRoutes
import com.example.routesapp.ui.RoutesViewModel
import com.example.routesapp.ui.components.RouteSummaryHeader
import com.example.routesapp.ui.theme.RoutesAppTheme

@Composable
fun RouteDetailsScreen(modifier: Modifier = Modifier, viewModel: RoutesViewModel = viewModel()) {
    val routeLookupError = viewModel.routeLookupError.collectAsState().value
    if (routeLookupError) {
        Text("Błąd: nie znaleziono trasy",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    } else {
        val route = viewModel.route.collectAsState().value
        if (route != null) {
            Column(modifier = modifier.padding(16.dp)) {
                RouteSummaryHeader(route.summary)
                Text(route.description)
            }
        } else {
            Text("Wybierz trasę by zobaczyć szczegóły",
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview(showBackground = true)
fun RouteDetailsScreenPreview() {
    RoutesAppTheme {
        val viewModel = RoutesViewModel(FakeRoutesRepository())
        viewModel.getRouteById(SampleRoutes.routeSummary.id)
        RouteDetailsScreen(Modifier.fillMaxSize(), viewModel)
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview(showBackground = true)
fun RouteDetailsScreenPreviewNotSelected() {
    RoutesAppTheme {
        val viewModel = RoutesViewModel(FakeRoutesRepository())
        RouteDetailsScreen(Modifier.fillMaxSize(), viewModel)
    }
}
@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview(showBackground = true)
fun RouteDetailsScreenNotFoundPreviewNotFound() {
    RoutesAppTheme {
        val viewModel = RoutesViewModel(FakeRoutesRepository())
        viewModel.getRouteById(-1)
        RouteDetailsScreen(Modifier.fillMaxSize(), viewModel)
    }
}
