package net.skycast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.skycast.application.WeatherParameters
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

    val repository by lazy { AppRepository(context = this) }
    val weatherApi by lazy { WeatherbitApi(key = "5ffd1f30c7974380947e096b644f842b") }
    val useCases by lazy { UseCaseImplementations(repository, weatherApi) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigator = rememberNavController()
            val homeViewModel = HomeViewModel(useCases)
            val favoritesViewModel = FavoritesViewModel(useCases)
            val historyViewModel = HistoryViewModel(useCases)
            LaunchedEffect("") {
                homeViewModel.initialize()
            }
            SkyCastTheme(darkTheme = false) {
                NavHost(navigator, startDestination = "home", modifier = Modifier.fillMaxSize()) {
                    composable("home") {
                        HomeView(homeViewModel)
                    }
                    composable("favorites") {
                        FavoritesView(favoritesViewModel)
                    }
                    composable("history") {
                        HistoryView(historyViewModel)
                    }
                }
            }
        }
    }
}
