package net.skycast.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.skycast.domain.WeatherData
import net.skycast.infrastructure.UseCaseImplementations

//Class: HistoryViewModel
//State Data Class: State
//Holds the weather history, loading status, and error information.
//State Flow: _stateFlow and stateFlow
//Manages the state of the weather history.
//Initialization: Fetches weather history data on initialization.
//Function: loadHistory
//Fetches weather history data and updates the state.
//Function: deleteRecord
//Deletes a specific weather history record and updates the state.
//Function: clearError
//Clears any error state.

class HistoryViewModel(
    private val useCases: UseCaseImplementations
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

    suspend fun deleteRecord(id: Long) {
        updateState { copy(isBusy = true) }
        try {
            useCases.RemoveWeatherData(id)
            updateState {
                copy(
                    history = history.filterKeys { it != id },
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

    suspend fun clearError() {
        updateState { copy(error = null) }
    }
}