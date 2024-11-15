package net.skycast.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.skycast.application.WeatherParameters
import net.skycast.domain.Location
import net.skycast.domain.WeatherData
import net.skycast.infrastructure.UseCaseImplementations

class HomeViewModel(
    val useCases: UseCaseImplementations,
) : ViewModel() {

    data class State(
        val data: WeatherData? = null,
        val favorites: Map<Long, Location> = emptyMap(),
        val isBusy: Boolean = false,
        val error: Throwable? = null,
    )

    private val _stateFlow = MutableStateFlow(State())
    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    private suspend inline fun updateState(transform: suspend State.() -> State) {
        _stateFlow.value = try {
            _stateFlow.value.transform()
        } catch (e: Throwable) {
            _stateFlow.value.copy(error = e, isBusy = false)
        }
    }
//HomePage Views Display Torronto Weather Data
    suspend fun initialize() {
        updateState {
            copy(isBusy = true)
        }
        updateState {
            val favorites = useCases.LoadFavoriteLocations(Unit)
            val parameters = if (favorites.isEmpty()) {
                WeatherParameters(
                    city = "Toronto",
                    country = "CA",
                    days = 7
                )
            } else {
                val location = favorites.values.first()
                WeatherParameters(
                    latitude = location.latitude,
                    longitude = location.longitude,
                )
            }
            val data = useCases.GetWeatherData(parameters)
            if (favorites.isEmpty()) {
                useCases.SaveFavoriteLocation(data.location)
                copy(data = data, favorites = useCases.LoadFavoriteLocations(Unit), isBusy = false, error = null)
            } else {
                copy(data = data, favorites = favorites, isBusy = false, error = null)
            }
        }
    }

    suspend fun refresh() {
        val location = _stateFlow.value.data?.location
        if (location != null) {
            refresh(location)
        }
    }

    suspend fun refresh(location: Location) {
        updateState {
            copy(isBusy = true)
        }
        updateState {
            copy(
                data = useCases.GetWeatherData(
                    WeatherParameters(
                        latitude = location.latitude,
                        longitude = location.longitude,
                    )
                ),
                isBusy = false,
                error = null,
            )
        }
    }

    fun clearError() {
        viewModelScope.launch {
            updateState { copy(error = null) }
        }
    }

    suspend fun save() {
        updateState {
            copy(isBusy = true)
        }
        updateState {
            if (data != null) {
                useCases.SaveWeatherData(data)
                copy(isBusy = false)
            } else {
                copy(isBusy = false)
            }
        }
    }

    // method to update weather data when selecting from favorites
    suspend fun updateWeatherData(weatherData: WeatherData) {
        updateState {
            copy(isBusy = true)
        }
        updateState {
            useCases.SaveWeatherData(weatherData)
            copy(
                data = weatherData,
                favorites = favorites,  // Keep existing favorites
                isBusy = false,
                error = null
            )
        }
    }

    // Optional: Add method to refresh favorites list (Important Feature)
    suspend fun refreshFavorites() {
        updateState {
            copy(
                favorites = useCases.LoadFavoriteLocations(Unit),
                error = null
            )
        }
    }

    // Optional: Add method to add location to favorites (Required)
    suspend fun addToFavorites(location: Location) {
        updateState {
            val id = useCases.SaveFavoriteLocation(location)
            copy(
                favorites = favorites + (id to location),
                error = null
            )
        }
    }

    // Optional: Add method to remove from favorites (Not Needed
    suspend fun removeFromFavorites(id: Long) {
        updateState {
            useCases.RemoveFavoriteLocation(id)
            copy(
                favorites = favorites - id,
                error = null
            )
        }
    }

    // Optional: Add method to check if location is in favorites
    fun isLocationInFavorites(location: Location): Boolean {
        return _stateFlow.value.favorites.values.any {
            it.latitude == location.latitude && it.longitude == location.longitude
        }
    }
}