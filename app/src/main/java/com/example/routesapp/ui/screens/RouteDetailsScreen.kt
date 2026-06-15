package com.example.routesapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.routesapp.data.fake.FakeRoutesRepository
import com.example.routesapp.data.fake.SampleRoutes
import com.example.routesapp.ui.viewmodels.RoutesViewModel
import com.example.routesapp.ui.viewmodels.StopwatchViewModel
import com.example.routesapp.ui.viewmodels.WorkoutsViewModel
import com.example.routesapp.ui.components.RouteHighscoresList
import com.example.routesapp.ui.components.RouteSummaryHeader
import com.example.routesapp.ui.theme.RoutesAppTheme

@Composable
fun RouteDetailsScreen(modifier: Modifier = Modifier,
                       routesViewModel: RoutesViewModel = viewModel(factory = RoutesViewModel.Factory),
                       workoutsViewModel: WorkoutsViewModel = viewModel(factory = WorkoutsViewModel.Factory)
) {
    val stopwatchViewModel: StopwatchViewModel = viewModel(factory = StopwatchViewModel.Factory)
    val routeLookupError = routesViewModel.routeLookupError.collectAsState().value
    if (routeLookupError) {
        Text("Błąd: nie znaleziono trasy",
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    } else {
        val route = routesViewModel.route.collectAsState().value
        var sheetOpen = remember { mutableStateOf(false) }
        if (route != null) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    workoutsViewModel.getFastestByRouteId(route.summary.id)
                    Column(modifier = modifier.padding(16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                    ) {
                        RouteSummaryHeader(route.summary)
                        val BASE_URL = "http://192.168.1.2:8080"
                        val imageURL = "$BASE_URL/routes/${route.summary.id}/image"
                        Box(modifier = Modifier.fillMaxWidth()) {
                            AsyncImage(model = imageURL,
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Text(route.description)
                        workoutsViewModel.getFastestByRouteId(route.summary.id)
                        RouteHighscoresList(
                            workoutsViewModel.workouts.collectAsState().value,
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        )
                    }
                    LargeFloatingActionButton(onClick = {
                        sheetOpen.value = !sheetOpen.value
                    },
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.BottomEnd).padding(32.dp)
                    ) {
                        if (stopwatchViewModel.isRunning.collectAsState().value) {
                            // TODO: animate the clock hand instead
                            Image(imageVector = Icons.Filled.Timer,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )
                        } else {
                            Image(imageVector = Icons.Outlined.Timer,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )

                        }
                    }
                }
                if (sheetOpen.value) {
                    StopwatchSheet(sheetOpen)
                }
            }
        } else {
            Text("Wybierz trasę by zobaczyć szczegóły",
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopwatchSheet(isOpen: MutableState<Boolean>) {
    ModalBottomSheet(onDismissRequest = {isOpen.value = false}) {
        StopwatchScreen()
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
