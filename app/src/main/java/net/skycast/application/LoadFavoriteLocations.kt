package net.skycast.application

import net.skycast.domain.Location

interface LoadFavoriteLocations : UseCase<Unit, Map<Long, Location>>
