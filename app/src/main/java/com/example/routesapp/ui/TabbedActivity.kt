package com.example.routesapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PedalBike
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.routesapp.RoutesApp
import com.example.routesapp.ui.screens.RouteDetailsScreen
import com.example.routesapp.ui.screens.RouteListScreen
import com.example.routesapp.ui.theme.RoutesAppTheme
import com.example.routesapp.data.SessionState
import com.example.routesapp.ui.components.RouteHighscoresList
import com.example.routesapp.ui.viewmodels.AuthViewModel
import com.example.routesapp.ui.viewmodels.RoutesViewModel
import com.example.routesapp.ui.viewmodels.WorkoutsViewModel
import com.example.shared.ActivityType
import com.example.shared.RouteType
import com.example.shared.RoutesQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
class TabbedActivity : ComponentActivity() {
    val combinedListWidthDp = 320.dp
    val masterDetailCutoffWidthDp = 1000.dp

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
                val pagerState = rememberPagerState(pageCount = {
                    3
                })
                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

                val routesViewModel: RoutesViewModel = viewModel(factory = RoutesViewModel.Factory)
                val route = routesViewModel.route.collectAsState().value

                BackHandler(enabled = (pagerState.currentPage == 1 && route != null) ) {
                    routesViewModel.forgetRoute()
                }

                ModalNavigationDrawer(drawerState = drawerState,
                    drawerContent = { NavDrawerContent(drawerState, pagerState) }
                ) {
                    val widthDp = LocalWindowInfo.current.containerDpSize.width
                    when {
                        widthDp < 600.dp -> CompactLayout(
                            pagerState,
                            drawerState,
                            scrollBehavior,
                            scope
                        )

                        widthDp < masterDetailCutoffWidthDp -> MediumLayout(
                            pagerState,
                            drawerState,
                            scrollBehavior,
                            scope
                        )

                        else -> ExpandedLayout(pagerState, drawerState, scrollBehavior, scope)
                    }
                }
            }
        }
    }

    @Composable
    fun CompactLayout(
        pagerState: PagerState,
        drawerState: DrawerState,
        scrollBehavior: TopAppBarScrollBehavior,
        scope: CoroutineScope
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = { TopBar(pagerState, drawerState, scope, scrollBehavior) },
            bottomBar = { NavBar(pagerState, scope) }
        ) { innerPadding ->
            ContentTabs(pagerState, modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .fillMaxSize()
                .padding(8.dp)
            // TODO: .clip(MaterialTheme.shapes.large)
            )
        }
    }

    @Composable
    fun MediumLayout(
        pagerState: PagerState,
        drawerState: DrawerState,
        scrollBehavior: TopAppBarScrollBehavior,
        scope: CoroutineScope
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.weight(1f).nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = { TopBar(pagerState, drawerState, scope, scrollBehavior) },
            ) { innerPadding ->
                ContentTabs(
                    pagerState,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            NavRailMedium(pagerState, scope, modifier = Modifier.align(Alignment.CenterVertically))
        }
    }

    @Composable
    fun ExpandedLayout(
        pagerState: PagerState,
        drawerState: DrawerState,
        scrollBehavior: TopAppBarScrollBehavior,
        scope: CoroutineScope
    )  {
        Row(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.weight(1f),
                topBar = { TopBar(pagerState, drawerState, scope, scrollBehavior) },
            ) { innerPadding ->
                ContentTabs(
                    pagerState,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
            NavRailExpanded(pagerState, scope, modifier = Modifier.align(Alignment.CenterVertically))
        }
    }

    @Composable
    fun TopBar(pagerState: PagerState, drawerState: DrawerState, scope: CoroutineScope, scrollBehavior: TopAppBarScrollBehavior) {
        val routesViewModel: RoutesViewModel = viewModel(factory = RoutesViewModel.Factory)
        val route = routesViewModel.route.collectAsState().value
        TopAppBar(
            scrollBehavior = scrollBehavior,
            title = {
                Text(
                    text = (when (pagerState.currentPage) {
                        0 -> "Routes App"
                        1 -> if (route != null) {
                            "Szczegóły"
                        } else {
                            "Trasy"
                        }
                        2 -> "Historia"
                        else -> ""
                    }),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
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
            actions = {
                IconButton(
                    enabled = pagerState.currentPage == 1 && route != null,
                    onClick = {routesViewModel.forgetRoute()}
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null )
                }
            }
        )
    }

    val navItems = listOf(
        Pair("Główna", Icons.Default.Home),
        Pair("Trasy", Icons.Default.Route),
        Pair("Historia", Icons.Default.History)
    )
    @Composable
    fun NavBar(pagerState: PagerState, scope: CoroutineScope) {
        NavigationBar {
            navItems.forEachIndexed { index, (title, icon) ->
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
        }
    }

    @Composable
    fun NavRailMedium(pagerState: PagerState, scope: CoroutineScope, modifier: Modifier = Modifier) {
        NavigationRail(
            modifier = modifier,
            windowInsets = WindowInsets.safeDrawing.only(
                WindowInsetsSides.Vertical
            )
        ) {
            Column(modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                navItems.forEachIndexed { index, (title, icon) ->
                    NavigationRailItem(
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
            }
        }
    }

    @Composable
    fun NavRailExpanded(pagerState: PagerState, scope: CoroutineScope, modifier: Modifier = Modifier) {
        NavigationRail(
            modifier = modifier,
            windowInsets = WindowInsets.safeDrawing.only(
                WindowInsetsSides.Vertical
            )
        ) {
            navItems.forEachIndexed { index, (title, icon) ->
                NavigationRailItem(
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
        }
    }


    @Composable
    fun ContentTabs(pagerState: PagerState, modifier: Modifier = Modifier) {
        val routesViewModel: RoutesViewModel = viewModel(factory = RoutesViewModel.Factory)
        val route = routesViewModel.route.collectAsState().value
        Column(modifier = modifier
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> MainScreen()
                    1 -> {
                        if (route == null) {
                            RouteListAdaptive(false, modifier = Modifier.fillMaxSize())
                        } else {
                            RouteDetailsScreen(modifier = Modifier.fillMaxSize())
                        }
                    }
                    2 -> WorkoutRecords()
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
        ModalDrawerSheet(modifier = Modifier.width(240.dp).fillMaxHeight().verticalScroll(rememberScrollState())) {
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
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text("Tymczasowy ekran główny", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }
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
    fun RouteListAdaptive(alternatingRowColours: Boolean, modifier: Modifier = Modifier) {
        val widthDp = LocalWindowInfo.current.containerDpSize.width
        if (widthDp < masterDetailCutoffWidthDp) {
            RouteList(alternatingRowColours, modifier = modifier)
        } else {
            RouteListAndDetails(alternatingRowColours, modifier = modifier)
        }
    }

    @Composable
    fun RouteList(alternatingRowColours: Boolean, modifier: Modifier = Modifier) {
        val routesViewModel: RoutesViewModel = viewModel(factory = RoutesViewModel.Factory)
        RouteListScreen(
            modifier = modifier,
            alternatingRowColours = alternatingRowColours,
            onClick = { route ->
                routesViewModel.getRouteById(route.id)
            }
        )
    }

    @Composable
    fun RouteListAndDetails(alternatingRowColours: Boolean, modifier: Modifier = Modifier) {
        val routesViewModel: RoutesViewModel = viewModel(factory = RoutesViewModel.Factory)
        Row(
            modifier = modifier
        ) {
            val selectedRoute = routesViewModel.route.collectAsState().value
            val selectedHighlightColour = MaterialTheme.colorScheme.tertiary

            RouteListScreen(
                modifier = Modifier.width(combinedListWidthDp),
                alternatingRowColours = alternatingRowColours,
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