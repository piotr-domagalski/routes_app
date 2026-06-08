package com.example.routesapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.R
import com.example.routesapp.ui.StopwatchViewModel
import com.example.routesapp.ui.theme.RoutesAppTheme

@Composable
fun StopwatchScreen(modifier: Modifier = Modifier) {
    val stopwatchViewModel: StopwatchViewModel = viewModel(factory = StopwatchViewModel.Factory)
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
        val buttonModifier = Modifier.size(64.dp).padding(0.dp)
        val buttonContentPadding = PaddingValues.Zero
        val imageModifier = Modifier.size(40.dp)
        Row(modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = {stopwatchViewModel.upload()},
                modifier = buttonModifier,
                contentPadding = buttonContentPadding,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_upload),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(LocalContentColor.current),
                    modifier = imageModifier
                )
            }
            Button(onClick = {
                stopwatchViewModel.toggle()
            },
                modifier = buttonModifier,
                contentPadding = buttonContentPadding,
                ) {
                if(stopwatchViewModel.isRunning.collectAsState().value) {
                    Image(
                        painter = painterResource(R.drawable.ic_stop),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(LocalContentColor.current),
                        modifier = imageModifier
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.ic_start),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(LocalContentColor.current),
                        modifier = imageModifier
                    )
                }
            }
            Button(onClick = {
                stopwatchViewModel.reset()
            },
                modifier = buttonModifier,
                contentPadding = buttonContentPadding,
                ) {
                Image(
                    painter = painterResource(R.drawable.ic_reset),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(LocalContentColor.current),
                    modifier = imageModifier
                )
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