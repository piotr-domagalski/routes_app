package com.example.routesapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.data.fake.FakeRoutesRepository
import com.example.routesapp.ui.RoutesViewModel
import com.example.routesapp.ui.StopwatchViewModel
import com.example.routesapp.ui.theme.RoutesAppTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@Composable
fun StopwatchScreen(modifier: Modifier = Modifier) {
    val stopwatchViewModel: StopwatchViewModel = viewModel()
    Column(modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("%01d:%02d:%02d".format(
                stopwatchViewModel.elapsedTime.collectAsState().value.inWholeHours,
                stopwatchViewModel.elapsedTime.collectAsState().value.inWholeMinutes % 60,
                stopwatchViewModel.elapsedTime.collectAsState().value.inWholeSeconds % 60,
            ),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Row(modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {},
                modifier = Modifier.weight(1.0f)
            ) {
                Text("upload")
            }
            Button(onClick = {
                stopwatchViewModel.toggle()
            },
                modifier = Modifier.weight(1.5f)
            ) {
                Text(if(stopwatchViewModel.isRunning.collectAsState().value) { "stop" } else { "start" })
            }
            Button(onClick = {
                stopwatchViewModel.reset()
            },
                modifier = Modifier.weight(1.0f)
            ) {
                Text("reset")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun StopwatchScreenPreview() {
    RoutesAppTheme {
        StopwatchScreen()
    }
}