package net.skycast.application

import net.skycast.domain.Location

interface GetAllFavoriteLocationsUseCase : UseCase<Unit, Map<Long, Location>> {
}