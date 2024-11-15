@file:Suppress("FunctionName")
@file:OptIn(ExperimentalMaterial3Api::class)

package net.skycast.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import net.skycast.ui.model.HomeViewModel
import java.util.Date
import java.text.SimpleDateFormat
import kotlin.math.roundToInt
//HomePage Views Display Torronto Weather Data
//Explanation : The HomeView composable function displays the weather data for a specific location.
// The location's name, current temperature, weather description, and forecast for the next seven days are displayed.
// The user can click on the search icon in the top app bar to navigate to the search screen.
// The weather data is displayed in a column layout with the location name, current temperature, weather description, and forecast displayed in separate Text composable functions.
// The forecast is displayed in a LazyColumn with each day's weather data displayed in a Row composable function.
// The weather data includes the day of the week, weather icon, weather description, minimum temperature, and maximum temperature.
// The user can click on the Save button to save the weather data to their favorites.
// The Save button is displayed at the bottom of the screen.
// The HomeView composable function takes a HomeViewModel and NavController as parameters.
// The HomeViewModel is used to get the weather data for the location, and the NavController is used to navigate to the search screen.
// The state of the HomeViewModel is collected using the collectAsState function, and the weather data is displayed based on the state.

val formatDayOfWeek = SimpleDateFormat("EEE")

fun Date.dayOfWeek(): String {
    return formatDayOfWeek.format(this)
}

fun Long.dayOfWeek(): String {
    return Date(this * 1000).dayOfWeek()
}

fun Double?.formatTemperature(): String {
    return this?.roundToInt()?.toString()?.plus("°") ?: ""
}

@Composable
fun HomeView(
    model: HomeViewModel,
    navController: NavController
) {
    val state by model.stateFlow.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather") },
                actions = {
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(innerPadding)
                .padding(horizontal = 8.dp, vertical = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val location = state.data?.location
            val current = state.data?.current
            val forecasts = state.data?.forecasts
            val maxTemperature = forecasts?.firstOrNull()?.maxTemperature?.roundToInt()
            val minTemperature = forecasts?.firstOrNull()?.minTemperature?.roundToInt()
            Text(
                text = (location?.cityName ?: ""),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = current?.temperature?.toString()?.plus("°") ?: "",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = current?.weatherDescription ?: "",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "L: ${minTemperature}°   H: ${maxTemperature}°",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleLarge
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Text(
                        text = "Forecast",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    HorizontalDivider()
                }
                forecasts?.withIndex()?.forEach { (index, forecast) ->
                    item {
                        Row(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(52.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.fillParentMaxWidth(0.15f),
                                text = if (index == 0) "Today" else forecast.timestamp.dayOfWeek(),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.titleLarge
                            )
                            if (forecast.weatherCode != null) {
                                Icon(
                                    painter = painterResource(id = getWeatherIcon(forecast.weatherCode)),
                                    contentDescription = "",
                                    modifier = Modifier.fillParentMaxWidth(0.15f)
                                )
                            } else {
                                Spacer(modifier = Modifier.fillParentMaxWidth(0.15f))
                            }
                            Text(
                                modifier = Modifier.fillParentMaxWidth(0.5f),
                                text = forecast.weatherDescription ?: "",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                modifier = Modifier.fillParentMaxWidth(0.1f),
                                text = forecast.minTemperature.formatTemperature(),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Right,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                modifier = Modifier.fillParentMaxWidth(0.1f),
                                text = forecast.maxTemperature.formatTemperature(),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Right,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        HorizontalDivider()
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(top = 32.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                // TODO
                            },
                        ) {
                            Text("Save", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }
        }
    }
}