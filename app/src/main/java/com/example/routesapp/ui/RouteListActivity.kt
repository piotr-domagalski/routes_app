package com.example.routesapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.routesapp.ui.theme.RoutesAppTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.example.routesapp.data.fake.FakeRoutesRepository
import com.example.routesapp.data.fake.SampleRoutes
import com.example.shared.ActivityType
import com.example.shared.RouteSummary
import com.example.shared.RouteType
import com.example.routesapp.R

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

@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview(showBackground = true)
fun RouteListPreview() {
    val viewModel = RoutesViewModel(
        repository = FakeRoutesRepository()
    )

    RoutesAppTheme {
        RouteList(viewModel)
    }
}

@Composable
fun RouteSummaryHeader(route: RouteSummary, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = route.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = modifier
                    .padding(8.dp)
                //.clickable {
                //    navController.navigate("details/$fruit")
                //}
            )
            Text(
                text = "${route.distanceMeters / 1000F}km",
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier
                    .padding(8.dp)
                //.clickable {
                //    navController.navigate("details/$fruit")
                //}
            )
        }
        ActivityTypeIcon(route.activityType)
        RouteTypeIcon(route.routeType)
    }
}

@Composable
fun RouteTypeIcon(type: RouteType) {
    val colourOn = MaterialTheme.colorScheme.onSurfaceVariant
    val colourOff = MaterialTheme.colorScheme.surfaceVariant
    val imageModifier = Modifier.size(48.dp)
    when (type) {
        RouteType.LOOP -> {
            Box {
                Image(
                    painter = painterResource(R.drawable.ic_oneway_grayout),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colourOff),
                    modifier = imageModifier
                )
                Image(
                    painter = painterResource(R.drawable.ic_loop),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colourOn),
                    modifier = imageModifier
                )
            }
        }
        RouteType.ONEWAY -> {
            Box {
                Image(
                    painter = painterResource(R.drawable.ic_loop_grayout),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colourOff),
                    modifier = imageModifier
                )
                Image(
                    painter = painterResource(R.drawable.ic_oneway),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colourOn),
                    modifier = imageModifier
                )
            }
        }
    }
}

@Composable
@Preview
fun RouteTypeIconPreview() {
    RoutesAppTheme {
        Row {
            RouteTypeIcon(RouteType.ONEWAY)
            RouteTypeIcon(RouteType.LOOP)
        }
    }
}

@Composable
fun ActivityTypeIcon(type: ActivityType, modifier: Modifier = Modifier) {
    val bike_colour = when (type) {
        ActivityType.RUN -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val shoe_colour = when (type) {
        ActivityType.BIKE -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.ic_bike),
            contentDescription = null,
            colorFilter = ColorFilter.tint(bike_colour),
            modifier = Modifier.size(32.dp)
        )
        Image(
            painter = painterResource(R.drawable.ic_shoe),
            contentDescription = null,
            colorFilter = ColorFilter.tint(shoe_colour),
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
@Preview(
    name = "Light",
    showBackground = true
)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun ActivityTypeIconPreview() {
    RoutesAppTheme {
        Row {
            ActivityTypeIcon(ActivityType.RUN)
            ActivityTypeIcon(ActivityType.BIKE)
            ActivityTypeIcon(ActivityType.BOTH)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun RouteSummaryHeaderPreview() {
    RoutesAppTheme {
        RouteSummaryHeader(SampleRoutes.routeSummary)
    }
}
