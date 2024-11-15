package net.skycast.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.skycast.application.WeatherParameters
import net.skycast.domain.WeatherData
import net.skycast.infrastructure.UseCaseImplementations

class HistoryViewModel(
    private val useCases: UseCaseImplementations,
    private val homeViewModel: HomeViewModel  // Add homeViewModel parameter
) : ViewModel() {

    data class State(
        val history: Map<Long, WeatherData> = emptyMap(),
        val isBusy: Boolean = false,
        val error: Throwable? = null
    )

    private val _stateFlow = MutableStateFlow(State())
    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            loadHistory()
        }
    }

    private suspend inline fun updateState(transform: State.() -> State) {
        _stateFlow.value = try {
            _stateFlow.value.transform()
        } catch (e: Throwable) {
            _stateFlow.value.copy(error = e, isBusy = false)
        }
    }

    suspend fun loadHistory() {
        updateState { copy(isBusy = true) }
        try {
            val weatherHistory = useCases.LoadWeatherData(Unit)
            updateState {
                copy(
                    history = weatherHistory,
                    isBusy = false,
                    error = null
                )
            }
        } catch (e: Exception) {
            updateState {
                copy(
                    isBusy = false,
                    error = e
                )
            }
        }
    }

    // Updated to fetch fresh weather data and update HomeViewModel
    suspend fun selectWeather(weatherData: WeatherData) {
        updateState { copy(isBusy = true) }
        try {
            // Fetch fresh weather data for the location
            val freshWeatherData = useCases.GetWeatherData(
                WeatherParameters(
                    latitude = weatherData.location.latitude,
                    longitude = weatherData.location.longitude
                )
            )
            // Update HomeViewModel with fresh data (Dont need to reopen the app)
            homeViewModel.updateWeatherData(freshWeatherData)
            updateState { copy(isBusy = false, error = null) }
        } catch (e: Exception) {
            updateState {
                copy(
                    isBusy = false,
                    error = e
                )
            }
            throw e
        }
    }

    suspend fun deleteRecord(id: Long) {
        try {
            useCases.RemoveWeatherData(id)
            updateState {
                copy(
                    history = history.filterKeys { it != id }
                )
            }
        } catch (e: Exception) {
            updateState { copy(error = e) }
        }
    }

    fun clearError() {
        _stateFlow.value = _stateFlow.value.copy(error = null)
    }
}