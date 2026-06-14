package com.example.routesapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.PedalBike
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.RoutesApp
import com.example.routesapp.ui.screens.RouteDetailsScreen
import com.example.routesapp.ui.screens.RouteListScreen
import com.example.routesapp.ui.theme.RoutesAppTheme
import com.example.routesapp.data.SessionState
import com.example.routesapp.ui.components.RouteHighscoresList
import com.example.shared.ActivityType
import com.example.shared.RouteType
import com.example.shared.RoutesQuery
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
class TabbedActivity : ComponentActivity() {
    val combinedListWidthDp = 320.dp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as RoutesApp
        val sessionState = app.appContainer.sessionManager.state.value
        if (sessionState !is SessionState.LoggedIn
            ) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }

        setContent {
            RoutesAppTheme {
                TabbedLayout()
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    fun TabbedLayout() {
        val pagerState = rememberPagerState(pageCount = {
            3
        })
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        ModalNavigationDrawer(drawerState = drawerState,
            drawerContent = { NavDrawerContent(drawerState, pagerState) }
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { TopAppBar(
                    title = { Text("Tab Title", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                    navigationIcon = { IconButton(
                        onClick = { scope.launch {
                            if (drawerState.isOpen) { drawerState.close() }
                            else { drawerState.open() }
                        }}
                    ) {
                        Icon(imageVector =
                            if (drawerState.isOpen) {
                                Icons.AutoMirrored.Filled.MenuOpen
                            }
                            else {
                                Icons.Default.Menu
                            },
                            contentDescription = null
                        )
                    }},
                    actions = { IconButton(onClick = {}) { Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null ) } }
                ) },
                bottomBar = { NavigationBar {
                    listOf(
                        Pair("Main", Icons.Default.Home),
                        Pair("Routes", Icons.Default.Route),
                        Pair("Workouts", Icons.Default.History)
                    ).forEachIndexed { index, (title, icon) ->
                        NavigationBarItem(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            icon = { Icon(imageVector = icon, contentDescription = null) },
                            label = { Text(title) }
                        )
                    }
                }}
            ) { innerPadding ->
                Column(modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .fillMaxSize()
                    .padding(8.dp)
                    // TODO: .clip(MaterialTheme.shapes.large)
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        when (page) {
                            0 -> MainScreen()
                            1 -> RouteListAdaptive(modifier = Modifier.fillMaxSize())
                            2 -> WorkoutRecords()
                        }
                    }
                }
            }

        }
    }


    @Composable
    fun NavDrawerContent(drawerState: DrawerState, pagerState: PagerState) {
        val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
        val routesViewModel: RoutesViewModel = viewModel(factory = RoutesViewModel.Factory)
        val scope = rememberCoroutineScope()
        val routeShortcutOnClickFactory = { query: RoutesQuery ->
            routesViewModel.setQuery(query)
            routesViewModel.loadRoutes()
            scope.launch { drawerState.close() }
            scope.launch { pagerState.animateScrollToPage(1) }
        }
        ModalDrawerSheet(modifier = Modifier.width(200.dp)) {
            val user = (authViewModel.sessionState.collectAsState().value as? SessionState.LoggedIn)?.user
            NavDrawerHeading(if (user != null) { "Elo elo, $user!" } else { "Wylogowano" })

            HorizontalDivider(modifier = Modifier.width(180.dp).align(Alignment.CenterHorizontally))

            NavDrawerHeading("Trasy")

            NavDrawerItem(Icons.Outlined.PedalBike, "Rowerowe",
                onClick = { routeShortcutOnClickFactory(RoutesQuery(activityType = ActivityType.BIKE)) }
            )
            NavDrawerItem(Icons.AutoMirrored.Outlined.DirectionsRun, "Biegowe",
                onClick = { routeShortcutOnClickFactory(RoutesQuery(activityType = ActivityType.RUN)) }
            )
            NavDrawerItem(Icons.Outlined.QuestionMark, "Pętle",
                onClick = { routeShortcutOnClickFactory(RoutesQuery(routeType = RouteType.LOOP)) }
            )
            NavDrawerItem(Icons.Outlined.Route, "Jednokierunkowe",
                onClick = { routeShortcutOnClickFactory(RoutesQuery(routeType = RouteType.ONEWAY)) }
            )
            NavDrawerItem(Icons.Outlined.QuestionMark, "Krótkie")
            NavDrawerItem(Icons.Outlined.QuestionMark, "Długie")

            HorizontalDivider(modifier = Modifier.width(180.dp).align(Alignment.CenterHorizontally))

            NavDrawerItem(Icons.Outlined.Info, "O aplikacji")
            NavDrawerItem(Icons.Outlined.Settings, "Ustawienia")

            if (user != null) {
                NavDrawerItem(Icons.AutoMirrored.Outlined.Logout, "Wyloguj", onClick = {
                    authViewModel.logout()
                })
            } else {
                val intent = Intent(LocalActivity.current, LoginActivity::class.java)
                NavDrawerItem(Icons.AutoMirrored.Outlined.Login, "Zaloguj", onClick = {
                    startActivity(intent)
                })
            }
        }
    }

    @Composable
    fun NavDrawerHeading(text: String, modifier: Modifier = Modifier) {
        Text(text, style = MaterialTheme.typography.headlineSmall, modifier = modifier.padding(8.dp))
    }

    @Composable
    fun NavDrawerItem(icon: ImageVector, text: String, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick?.invoke() }
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Text(text)
        }
    }

    @Composable
    fun MainScreen() {
        Text("Main screen")
    }

    @Composable
    fun WorkoutRecords() {
        val workoutsViewModel: WorkoutsViewModel = viewModel(factory = WorkoutsViewModel.Factory)
        val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
        val user = (authViewModel.sessionState.collectAsState().value as? SessionState.LoggedIn)?.user
        if (user != null) {
            workoutsViewModel.getRecentByUser(user)
            RouteHighscoresList(workoutsViewModel.workouts.collectAsState().value)
        } else {
            Text("Log in to view workout records")
        }
    }
    @Composable
    fun RouteListAdaptive(modifier: Modifier = Modifier) {
        val displayMetrics: DisplayMetrics = LocalResources.current.displayMetrics
        val dpWidth = (displayMetrics.widthPixels / displayMetrics.density).dp

        if (dpWidth >= combinedListWidthDp*2) {
            RouteListAndDetails(modifier = modifier)
        } else {
            RouteListOnly(modifier = modifier)
        }
    }

    @Composable
    fun RouteListOnly(modifier: Modifier = Modifier) {
        val context = LocalContext.current
        RouteListScreen(
            modifier = modifier,
            alternatingRowColours = true,
            onClick = { route ->
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

    @Composable
    fun RouteListAndDetails(modifier: Modifier = Modifier) {
        val routesViewModel: RoutesViewModel = viewModel(factory = RoutesViewModel.Factory)
        Row(
            modifier = modifier
        ) {
            val selectedRoute = routesViewModel.route.collectAsState().value
            val selectedHighlightColour = MaterialTheme.colorScheme.tertiary

            RouteListScreen(
                modifier = Modifier.width(combinedListWidthDp),
                alternatingRowColours = true,
                onClick = { route ->
                    if (routesViewModel.route.value?.summary?.id == route.id) {
                        routesViewModel.forgetRoute()
                    } else {
                        routesViewModel.getRouteById(route.id)
                    }
                },
                highlightCallback = { route ->
                    if (selectedRoute != null && selectedRoute.summary.id == route.id) {
                        selectedHighlightColour
                    } else { null }
                }
            )
            val route = routesViewModel.route.collectAsState().value
            RouteDetailsScreen()
        }
    }
}