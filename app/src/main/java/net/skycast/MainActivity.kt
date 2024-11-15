package net.skycast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import net.skycast.infrastructure.UseCaseImplementations
import net.skycast.infrastructure.room.AppRepository
import net.skycast.infrastructure.weatherbit.WeatherbitApi
import net.skycast.ui.FavoritesView
import net.skycast.ui.HistoryView
import net.skycast.ui.HomeView
import net.skycast.ui.SearchView
import net.skycast.ui.model.FavoritesViewModel
import net.skycast.ui.model.HistoryViewModel
import net.skycast.ui.model.HomeViewModel
import net.skycast.ui.model.SearchViewModel
import net.skycast.ui.theme.SkyCastTheme

class MainActivity : ComponentActivity() {
    private val repository by lazy { AppRepository(context = this) }
    private val weatherApi by lazy { WeatherbitApi(key = "5ffd1f30c7974380947e096b644f842b") }
    private val useCases by lazy { UseCaseImplementations(repository, weatherApi) }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val navigator = rememberNavController()
            val navBackStackEntry by navigator.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // Initialize ViewModels with proper dependencies
            val homeViewModel: HomeViewModel = viewModel { //
                HomeViewModel(useCases)
            }
            val historyViewModel: HistoryViewModel = viewModel {
                HistoryViewModel(useCases, homeViewModel)
            }

            // Initialize initial data load
            LaunchedEffect(Unit) {
                homeViewModel.initialize()
            }

            SkyCastTheme {
                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        // Phone layout with bottom navigation
                        Scaffold(
                            bottomBar = {
                                if (currentRoute != "search") {
                                    NavigationBar {
                                        NavigationBarItem(
                                            selected = currentRoute == "home",
                                            onClick = {
                                                navigator.navigate("home") {
                                                    popUpTo("home") { inclusive = true }
                                                }
                                            },
                                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                            label = { Text("Home") }
                                        )
                                        NavigationBarItem(
                                            selected = currentRoute == "favorites",
                                            onClick = {
                                                navigator.navigate("favorites") {
                                                    popUpTo("favorites") { inclusive = true }
                                                }
                                            },
                                            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                                            label = { Text("Favorites") }
                                        )
                                        NavigationBarItem(
                                            selected = currentRoute == "history",
                                            onClick = {
                                                navigator.navigate("history") {
                                                    popUpTo("history") { inclusive = true }
                                                }
                                            },
                                            icon = { Icon(Icons.Default.History, contentDescription = "History") },
                                            label = { Text("History") }
                                        )
                                    }
                                }
                            }
                        ) { innerPadding ->
                            NavHost(
                                navigator,
                                startDestination = "home",
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable("home") {
                                    HomeView(homeViewModel, navigator)
                                }
                                composable("favorites") {
                                    FavoritesView(
                                        viewModel = viewModel {
                                            FavoritesViewModel(useCases, homeViewModel)
                                        },
                                        navController = navigator
                                    )
                                }
                                composable("history") {
                                    HistoryView(historyViewModel, navigator)
                                }
                                composable("search") {
                                    SearchView(
                                        viewModel = viewModel {
                                            SearchViewModel(useCases, homeViewModel)
                                        },
                                        navController = navigator
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        // Tablet/Foldable layout with navigation rail
                        Row(modifier = Modifier.fillMaxSize()) {
                            if (currentRoute != "search") {
                                NavigationRail(
                                    modifier = Modifier.padding(top = 56.dp),
                                    containerColor = MaterialTheme.colorScheme.inverseOnSurface
                                ) {
                                    NavigationRailItem(
                                        selected = currentRoute == "home",
                                        onClick = {
                                            navigator.navigate("home") {
                                                popUpTo("home") { inclusive = true }
                                            }
                                        },
                                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                        label = { Text("Home") }
                                    )
                                    NavigationRailItem(
                                        selected = currentRoute == "favorites",
                                        onClick = {
                                            navigator.navigate("favorites") {
                                                popUpTo("favorites") { inclusive = true }
                                            }
                                        },
                                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                                        label = { Text("Favorites") }
                                    )
                                    NavigationRailItem(
                                        selected = currentRoute == "history",
                                        onClick = {
                                            navigator.navigate("history") {
                                                popUpTo("history") { inclusive = true }
                                            }
                                        },
                                        icon = { Icon(Icons.Default.History, contentDescription = "History") },
                                        label = { Text("History") }
                                    )
                                }
                            }

                            NavHost(
                                navigator,
                                startDestination = "home",
                                modifier = Modifier.weight(1f)
                            ) {
                                composable("home") {
                                    HomeView(homeViewModel, navigator)
                                }
                                composable("favorites") {
                                    FavoritesView(
                                        viewModel = viewModel {
                                            FavoritesViewModel(useCases, homeViewModel)
                                        },
                                        navController = navigator
                                    )
                                }
                                composable("history") {
                                    HistoryView(historyViewModel, navigator)
                                }
                                composable("search") {
                                    SearchView(
                                        viewModel = viewModel {
                                            SearchViewModel(useCases, homeViewModel)
                                        },
                                        navController = navigator
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}