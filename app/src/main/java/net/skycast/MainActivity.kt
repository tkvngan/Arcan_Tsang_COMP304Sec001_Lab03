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
import net.skycast.ui.model.FavoritesViewModel
import net.skycast.ui.model.HistoryViewModel
import net.skycast.ui.model.HomeViewModel
import net.skycast.ui.theme.SkyCastTheme

class MainActivity : ComponentActivity() {
    private val repository by lazy { AppRepository(context = this) }
    private val weatherApi by lazy { WeatherbitApi(key = "7d1e78e060974166a89072938cd2b335") }
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

            // Initialize ViewModels
            val homeViewModel: HomeViewModel = viewModel { HomeViewModel(useCases) }
            val favoritesViewModel: FavoritesViewModel = viewModel { FavoritesViewModel(useCases) }
            val historyViewModel: HistoryViewModel = viewModel { HistoryViewModel(useCases) }

            // Initialize data
            LaunchedEffect(Unit) {
                homeViewModel.initialize()
            }

            SkyCastTheme {
                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        // Phone layout with bottom navigation
                        Scaffold(
                            bottomBar = {
                                NavigationBar {
                                    NavigationBarItem(
                                        selected = currentRoute == "home",
                                        onClick = { navigator.navigate("home") },
                                        icon = { Icon(Icons.Default.Home, "Home") },
                                        label = { Text("Home") }
                                    )
                                    NavigationBarItem(
                                        selected = currentRoute == "favorites",
                                        onClick = { navigator.navigate("favorites") },
                                        icon = { Icon(Icons.Default.Favorite, "Favorites") },
                                        label = { Text("Favorites") }
                                    )
                                    NavigationBarItem(
                                        selected = currentRoute == "history",
                                        onClick = { navigator.navigate("history") },
                                        icon = { Icon(Icons.Default.History, "History") },
                                        label = { Text("History") }
                                    )
                                }
                            }
                        ) { innerPadding ->
                            NavHost(
                                navigator,
                                startDestination = "home",
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable("home") { HomeView(homeViewModel) }
                                composable("favorites") { FavoritesView(favoritesViewModel) }
                                composable("history") { HistoryView(historyViewModel) }
                            }
                        }
                    }
                    else -> {
                        // Tablet/Foldable layout with navigation rail
                        Row(modifier = Modifier.fillMaxSize()) {
                            NavigationRail(
                                modifier = Modifier.padding(top = 56.dp),
                                containerColor = MaterialTheme.colorScheme.inverseOnSurface
                            ) {
                                NavigationRailItem(
                                    selected = currentRoute == "home",
                                    onClick = { navigator.navigate("home") },
                                    icon = { Icon(Icons.Default.Home, "Home") },
                                    label = { Text("Home") }
                                )
                                NavigationRailItem(
                                    selected = currentRoute == "favorites",
                                    onClick = { navigator.navigate("favorites") },
                                    icon = { Icon(Icons.Default.Favorite, "Favorites") },
                                    label = { Text("Favorites") }
                                )
                                NavigationRailItem(
                                    selected = currentRoute == "history",
                                    onClick = { navigator.navigate("history") },
                                    icon = { Icon(Icons.Default.History, "History") },
                                    label = { Text("History") }
                                )
                            }

                            // Main content area
                            NavHost(
                                navigator,
                                startDestination = "home",
                                modifier = Modifier.weight(1f)
                            ) {
                                composable("home") { HomeView(homeViewModel) }
                                composable("favorites") { FavoritesView(favoritesViewModel) }
                                composable("history") { HistoryView(historyViewModel) }
                            }
                        }
                    }
                }
            }
        }
    }
}