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

class FavoritesViewModel(
    private val useCases: UseCaseImplementations,
    private val homeViewModel: HomeViewModel
) : ViewModel() {

    data class State(
        val favorites: Map<Long, Location> = emptyMap(),
        val isBusy: Boolean = false,
        val error: Throwable? = null
    )

    private val _stateFlow = MutableStateFlow(State())
    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _stateFlow.value = _stateFlow.value.copy(isBusy = true)
            try {
                val favorites = useCases.LoadFavoriteLocations(Unit)
                _stateFlow.value = _stateFlow.value.copy(
                    favorites = favorites,
                    isBusy = false,
                    error = null
                )
            } catch (e: Exception) {
                _stateFlow.value = _stateFlow.value.copy(
                    error = e,
                    isBusy = false
                )
            }
        }
    }

    suspend fun selectLocation(location: Location) {
        _stateFlow.value = _stateFlow.value.copy(isBusy = true)
        try {
            val weatherData = useCases.GetWeatherData(
                WeatherParameters(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )
            // Update HomeViewModel with the new weather data
            homeViewModel.updateWeatherData(weatherData)
            _stateFlow.value = _stateFlow.value.copy(isBusy = false, error = null)
        } catch (e: Exception) {
            _stateFlow.value = _stateFlow.value.copy(
                error = e,
                isBusy = false
            )
            throw e
        }
    }

    suspend fun removeFavorite(id: Long) {
        try {
            useCases.RemoveFavoriteLocation(id)
            _stateFlow.value = _stateFlow.value.copy(
                favorites = _stateFlow.value.favorites - id
            )
        } catch (e: Exception) {
            _stateFlow.value = _stateFlow.value.copy(error = e)
        }
    }

    fun clearError() {
        _stateFlow.value = _stateFlow.value.copy(error = null)
    }
}