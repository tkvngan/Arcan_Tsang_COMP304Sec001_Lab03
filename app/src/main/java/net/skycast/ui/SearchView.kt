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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import net.skycast.domain.Location
import net.skycast.ui.model.SearchViewModel
// Fixed : Viewing Weather Details from Search
//Explanation : The SearchView composable function displays a search bar where the user can enter the name of a city to search for.
// The search results are displayed in a LazyColumn with each city displayed in a CityListItem composable function.
// The CityListItem composable function displays the city's name, state, and country.
// The user can click on a city to view its weather details or click on the favorite icon to add or remove it from their favorites.
// The search results are based on the user's input, and the user can clear the search query by clicking on the clear icon.
// The SearchView composable function takes a SearchViewModel and NavController as parameters.
// The SearchViewModel is used to search for cities and manage the search results, and the NavController is used to navigate back to the home screen.
// The state of the SearchViewModel is collected using the collectAsState function, and the search results are displayed based on the state.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    viewModel: SearchViewModel,
    navController: NavController
) {
    val state by viewModel.stateFlow.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search City") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search TextField
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchCity(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Enter city name...") },
                leadingIcon = { Icon(Icons.Default.Search, "Search") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, "Clear")
                        }
                    }
                }
            )

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    Text(
                        text = state.error?.message ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                state.searchResults.isEmpty() && searchQuery.isNotEmpty() -> {
                    Text(
                        text = "No cities found",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn {
                        items(state.searchResults) { location ->
                            CityListItem(
                                location = location,
                                isFavorite = state.favorites.values.any {
                                    it.latitude == location.latitude &&
                                            it.longitude == location.longitude
                                },
                                onItemClick = {
                                    scope.launch {
                                        viewModel.selectCity(location)
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    }
                                },
                                onFavoriteClick = {
                                    scope.launch {
                                        viewModel.toggleFavorite(location)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CityListItem(
    location: Location,
    isFavorite: Boolean,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onItemClick),
        headlineContent = {
            Text(location.cityName ?: "Unknown City")
        },
        supportingContent = {
            Text(
                buildString {
                    append(location.stateCode ?: "")
                    if (location.stateCode != null && location.countryCode != null) {
                        append(", ")
                    }
                    append(location.countryCode ?: "")
                }
            )
        },
        trailingContent = {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}