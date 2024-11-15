package net.skycast.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.skycast.application.WeatherParameters
import net.skycast.domain.Location
import net.skycast.infrastructure.UseCaseImplementations

class SearchViewModel(
    private val useCases: UseCaseImplementations,
    private val homeViewModel: HomeViewModel  // Add homeViewModel parameter
) : ViewModel() {

    data class State(
        val searchResults: List<Location> = emptyList(),
        val favorites: Map<Long, Location> = emptyMap(),
        val isLoading: Boolean = false,
        val error: Throwable? = null
    )

    private val _stateFlow = MutableStateFlow(State())
    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                val favorites = useCases.LoadFavoriteLocations(Unit)
                _stateFlow.value = _stateFlow.value.copy(favorites = favorites)
            } catch (e: Exception) {
                _stateFlow.value = _stateFlow.value.copy(error = e)
            }
        }
    }

    fun searchCity(query: String) {
        if (query.length < 2) {
            _stateFlow.value = _stateFlow.value.copy(searchResults = emptyList())
            return
        }

        viewModelScope.launch {
            _stateFlow.value = _stateFlow.value.copy(isLoading = true)
            try {
                val weatherData = useCases.GetWeatherData(
                    WeatherParameters(city = query)
                )
                _stateFlow.value = _stateFlow.value.copy(
                    searchResults = listOf(weatherData.location),
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _stateFlow.value = _stateFlow.value.copy(
                    isLoading = false,
                    error = e
                )
            }
        }
    }

    fun toggleFavorite(location: Location) {
        viewModelScope.launch {
            try {
                val existingFavorite = _stateFlow.value.favorites.entries.find {
                    it.value.latitude == location.latitude &&
                            it.value.longitude == location.longitude
                }

                if (existingFavorite != null) {
                    useCases.RemoveFavoriteLocation(existingFavorite.key)
                    _stateFlow.value = _stateFlow.value.copy(
                        favorites = _stateFlow.value.favorites - existingFavorite.key
                    )
                } else {
                    val id = useCases.SaveFavoriteLocation(location)
                    _stateFlow.value = _stateFlow.value.copy(
                        favorites = _stateFlow.value.favorites + (id to location)
                    )
                }
                loadFavorites()
            } catch (e: Exception) {
                _stateFlow.value = _stateFlow.value.copy(error = e)
            }
        }
    }

    fun selectCity(location: Location) {
        viewModelScope.launch {
            try {
                val weatherData = useCases.GetWeatherData(
                    WeatherParameters(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                )
                homeViewModel.updateWeatherData(weatherData)
            } catch (e: Exception) {
                _stateFlow.value = _stateFlow.value.copy(error = e)
            }
        }
    }
}