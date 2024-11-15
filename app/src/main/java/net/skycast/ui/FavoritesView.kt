package net.skycast.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import net.skycast.domain.Location
import net.skycast.ui.model.FavoritesViewModel

// Fixed : Viewing Weather Details from Favorites
//Explanation : The FavoritesView composable function displays a list of favorite locations.
// Each location is displayed in a FavoriteLocationCard composable function, which shows the location's name, state, and country.
// The user can click on a location to view its weather details or click on the delete icon to remove it from the favorites list.
// If there are no favorite locations, an EmptyFavoritesContent composable function is displayed, prompting the user to search for locations to add to their favorites.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesView(
    viewModel: FavoritesViewModel,
    navController: NavController
) {
    val state by viewModel.stateFlow.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite Locations") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.isBusy) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.favorites.isEmpty()) {
                EmptyFavoritesContent(
                    onSearchClick = { navController.navigate("search") }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.favorites.toList(),
                        key = { it.first }
                    ) { (id, location) ->
                        FavoriteLocationCard(
                            location = location,
                            onLocationClick = {
                                scope.launch {
                                    try {
                                        viewModel.selectLocation(location)
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    } catch (e: Exception) {
                                        // Error is handled by the error dialog
                                    }
                                }
                            },
                            onRemoveClick = {
                                scope.launch {
                                    viewModel.removeFavorite(id)
                                }
                            }
                        )
                    }
                }
            }
        }

        // Error handling (Basic)
        state.error?.let { error ->
            AlertDialog(
                onDismissRequest = { viewModel.clearError() },
                title = { Text("Error") },
                text = { Text(error.message ?: "An unknown error occurred") },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
// (Next Feature)
@Composable
private fun EmptyFavoritesContent(
    onSearchClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Favorite,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No favorite locations yet",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Add locations to quickly access their weather",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onSearchClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Search Locations")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteLocationCard(
    location: Location,
    onLocationClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onLocationClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = location.cityName ?: "Unknown Location",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!location.stateCode.isNullOrEmpty() || !location.countryCode.isNullOrEmpty()) {
                    Text(
                        text = buildString {
                            location.stateCode?.let { append(it) }
                            if (!location.stateCode.isNullOrEmpty() && !location.countryCode.isNullOrEmpty()) {
                                append(", ")
                            }
                            location.countryCode?.let { append(it) }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = onRemoveClick) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Remove from favorites",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}