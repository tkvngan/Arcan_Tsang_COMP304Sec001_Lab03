package net.skycast.ui.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val state = MutableStateFlow<State>(State())

    val stateFlow: StateFlow<State> = state.asStateFlow()

    private suspend inline fun updateState(transform: suspend State.() -> State) {
        state.value = try {
            state.value.transform()
        } catch (e: Throwable) {
            state.value.copy(error = e, isBusy = false)
        }
    }

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
        val location = state.value.data?.location
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

    suspend fun clearError() {
        updateState { copy(error = null) }
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
}