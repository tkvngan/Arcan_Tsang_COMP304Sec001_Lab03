package net.skycast.ui.model

import androidx.lifecycle.ViewModel
import net.skycast.domain.WeatherData
import net.skycast.domain.WeatherInfo
import net.skycast.infrastructure.UseCaseImplementations

class HistoryViewModel(val useCases: UseCaseImplementations) : ViewModel() {

    data class State(
        val history: Map<Long, WeatherData> = emptyMap()
        // TODO
    )
}