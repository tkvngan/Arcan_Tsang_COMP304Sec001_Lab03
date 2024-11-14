package net.skycast.ui

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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.skycast.domain.WeatherData
import net.skycast.ui.model.HistoryViewModel
import kotlin.math.roundToInt

//Key Features:
//Displays a list of weather history records.
//Shows a loading indicator when data is being fetched.
//Displays a message when no history is available.
//Allows users to delete individual history records.
//Handles errors by showing an alert dialog.

//State Management: Uses collectAsState to observe the state from HistoryViewModel.
//Loading Indicator: Displays a CircularProgressIndicator when data is being fetched.
//Empty State: Shows a message when no weather history is available.
//History List: Uses LazyColumn to display a list of weather history records.
//Error Handling: Displays an AlertDialog for any errors encountered.
//Helper Function: HistoryCard
//Displays individual weather history records with delete functionality.
//Helper Function: WeatherInfoColumn
//Displays weather information in a column format.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryView(model: HistoryViewModel) {
    val state by model.stateFlow.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather History") },
                navigationIcon = {
                    IconButton(onClick = {
                        // Navigation will be handled by NavController
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        if (state.isBusy) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
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
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
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
                        }
                    )
                }
            }
        }
    }

    // Error handling
    state.error?.let { error ->
        AlertDialog(
            onDismissRequest = {
                scope.launch {
                    model.clearError()
                }
            },
            title = { Text("Error") },
            text = { Text(error.localizedMessage ?: "An unknown error occurred") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            model.clearError()
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun HistoryCard(
    weatherData: WeatherData,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = weatherData.location.cityName ?: "Unknown Location",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    weatherData.location.stateCode?.let { stateCode ->
                        Text(
                            text = stateCode,
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

            Spacer(modifier = Modifier.height(8.dp))

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
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = weatherData.current.weatherDescription ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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