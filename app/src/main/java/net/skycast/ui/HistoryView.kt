package net.skycast.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import net.skycast.domain.WeatherData
import net.skycast.ui.model.HistoryViewModel
import kotlin.math.roundToInt
// Fixed : Viewing Weather Details from History
//Explanation : The HistoryView composable function displays a list of weather history records.
// Each record is displayed in a HistoryCard composable function,
// which shows the location's name, state, country, temperature, humidity, wind speed, weather icon, and weather description.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryView(
    model: HistoryViewModel,
    navController: NavController
) {
    val state by model.stateFlow.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather History") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        when {
            state.isBusy -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.history.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "No weather history available",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Weather data will appear here when you save locations",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(state.history.toList()) { (id, weatherData) ->
                        HistoryCard(
                            weatherData = weatherData,
                            onDelete = {
                                scope.launch {
                                    model.deleteRecord(id)
                                }
                            },
                            onClick = {
                                scope.launch {
                                    try {
                                        model.selectWeather(weatherData)
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    } catch (e: Exception) {
                                        // Error handled by error dialog
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }

        // Error handling
        state.error?.let { error ->
            AlertDialog(
                onDismissRequest = { model.clearError() },
                title = { Text("Error") },
                text = { Text(error.message ?: "An unknown error occurred") },
                confirmButton = {
                    TextButton(onClick = { model.clearError() }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryCard(
    weatherData: WeatherData,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = weatherData.location.cityName ?: "Unknown Location",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!weatherData.location.stateCode.isNullOrEmpty() ||
                        !weatherData.location.countryCode.isNullOrEmpty()) {
                        Text(
                            text = buildString {
                                weatherData.location.stateCode?.let { append(it) }
                                if (!weatherData.location.stateCode.isNullOrEmpty() &&
                                    !weatherData.location.countryCode.isNullOrEmpty()) {
                                    append(", ")
                                }
                                weatherData.location.countryCode?.let { append(it) }
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete record",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WeatherInfoColumn(
                    "Temperature",
                    "${weatherData.current.temperature.roundToInt()}°"
                )
                WeatherInfoColumn(
                    "Humidity",
                    "${weatherData.current.humidity?.roundToInt() ?: "N/A"}%"
                )
                WeatherInfoColumn(
                    "Wind",
                    "${weatherData.current.windSpeed?.roundToInt() ?: "N/A"} m/s"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                weatherData.current.weatherCode?.let { code ->
                    Icon(
                        painter = painterResource(id = getWeatherIcon(code)),
                        contentDescription = weatherData.current.weatherDescription,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = weatherData.current.weatherDescription ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun WeatherInfoColumn(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium
        )
    }
}