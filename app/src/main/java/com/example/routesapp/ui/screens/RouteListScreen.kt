package com.example.routesapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.ui.RouteSearchViewModel
import com.example.routesapp.ui.RoutesViewModel
import com.example.routesapp.ui.components.RouteSummaryGridTile
import com.example.routesapp.ui.components.RouteSummaryHeader
import com.example.shared.ActivityType
import com.example.shared.RouteSummary
import com.example.shared.RouteType
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteListScreen(
    modifier: Modifier = Modifier,

    alternatingRowColours: Boolean = false,
    onClick: ((RouteSummary) -> Unit)? = null,
    highlightCallback: ((RouteSummary) -> Color?)? = null
) {
    val routesViewModel: RoutesViewModel = viewModel(factory = RoutesViewModel.Factory)
    val searchViewModel: RouteSearchViewModel = viewModel(factory = RouteSearchViewModel.Factory)
    Column(modifier = modifier) {
        DockedSearchBar(
            inputField = { TextField(
                value = searchViewModel.searchString.collectAsState().value ?: "",
                onValueChange = { new: String -> searchViewModel.setSearchString(new) },
                placeholder = { Text("Search names & descriptions...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            searchViewModel.showFilters.value = !searchViewModel.showFilters.value
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    searchViewModel.updateQuery(0, 100)
                    routesViewModel.loadRoutes()
                }),
                modifier = Modifier.fillMaxWidth()
            ) },
            expanded = false,
            onExpandedChange = {},
            modifier = Modifier.fillMaxWidth()
        ) {
        }
        RouteList(alternatingRowColours, onClick, highlightCallback, modifier = Modifier.fillMaxSize())
    }
    if(searchViewModel.showFilters.collectAsState().value) {
        FilterSheet()
    }
}

@Composable
fun RouteList(
    alternatingRowColours: Boolean,
    onClick: ((RouteSummary) -> Unit)?,
    highlightCallback: ((RouteSummary) -> Color?)?,
    modifier: Modifier = Modifier,
) {
    val viewModel: RoutesViewModel = viewModel(factory = RoutesViewModel.Factory)
    val routes by viewModel.routes.collectAsState()

    val listState = rememberLazyListState()
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex >= totalItemsCount - 1
        }.combine(viewModel.routes) { atEnd, list ->
            Pair(atEnd, list.size)
        }
            .distinctUntilChanged()
            .collect { (atEnd, _) -> if (atEnd) {viewModel.loadMoreRoutes()} }
    }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(routes) { index, route ->
            val bg = highlightCallback?.invoke(route) ?:
                if (!alternatingRowColours || index % 2 == 0) {
                    MaterialTheme.colorScheme.surfaceContainer
                } else {
                    MaterialTheme.colorScheme.surfaceContainerLow
                }
            Surface(
                color = bg
            ) {
                RouteSummaryGridTile(
                    route,
                    modifier = Modifier.clickable {
                        onClick?.invoke(route)
                    }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FilterSheet(modifier: Modifier = Modifier) {
    val searchViewModel: RouteSearchViewModel = viewModel()
    ModalBottomSheet(
        onDismissRequest = { searchViewModel.showFilters.value = false },
        modifier = modifier
    ) {
        FilterRow() {
            Text("Activity:")
            FilterChip(selected = searchViewModel.activityType.collectAsState().value == ActivityType.BIKE,
                label = { Text("Bike") },
                onClick = {
                    if (searchViewModel.activityType.value == ActivityType.BIKE) {
                        searchViewModel.activityType.value = null
                    } else {
                        searchViewModel.activityType.value = ActivityType.BIKE
                    }
                }
            )
            FilterChip(selected = searchViewModel.activityType.collectAsState().value == ActivityType.RUN,
                label = { Text("Run") },
                onClick = {
                    if (searchViewModel.activityType.value == ActivityType.RUN) {
                        searchViewModel.activityType.value = null
                    } else {
                        searchViewModel.activityType.value = ActivityType.RUN
                    }
                }
            )
            FilterChip(selected = searchViewModel.activityType.collectAsState().value == ActivityType.BOTH,
                label = { Text("Both") },
                onClick = {
                    if (searchViewModel.activityType.value == ActivityType.BOTH) {
                        searchViewModel.activityType.value = null
                    } else {
                        searchViewModel.activityType.value = ActivityType.BOTH
                    }
                }
            )
            FilterChip(selected = searchViewModel.activityType.collectAsState().value == null,
                label = { Text("Any") },
                onClick = { searchViewModel.activityType.value = null }
            )
        }
        FilterRow() {
            Text("Route type:")
            FilterChip(selected = searchViewModel.routeType.collectAsState().value == RouteType.ONEWAY,
                label = { Text("One way") },
                onClick = {
                    if (searchViewModel.routeType.value == RouteType.ONEWAY) {
                        searchViewModel.routeType.value = null
                    } else {
                        searchViewModel.routeType.value = RouteType.ONEWAY
                    }
                }
            )
            FilterChip(selected = searchViewModel.routeType.collectAsState().value == RouteType.LOOP,
                label = { Text("Loop") },
                onClick = {
                    if (searchViewModel.routeType.value == RouteType.LOOP) {
                        searchViewModel.routeType.value = null
                    } else {
                        searchViewModel.routeType.value = RouteType.LOOP
                    }
                }
            )
            FilterChip(selected = searchViewModel.routeType.collectAsState().value == null,
                label = { Text("Either") },
                onClick = { searchViewModel.routeType.value = null }
            )
        }
        FilterRow() {
            Text("Distance:")
            RangeSlider(value = 0f.rangeTo(10f), onValueChange = {})
        }
    }
}

@Composable
fun FilterRow(content: @Composable (RowScope.() -> Unit)) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        content()
    }
}
