package net.skycast.ui.model

import androidx.lifecycle.ViewModel
import net.skycast.domain.Location
import net.skycast.infrastructure.UseCaseImplementations

class FavoritesViewModel(val useCases: UseCaseImplementations) : ViewModel() {

    data class State(
        val favorites: Map<Long, Location> = emptyMap()
        // TODO
    )

}