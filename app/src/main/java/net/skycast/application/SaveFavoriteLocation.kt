package net.skycast.application

import net.skycast.domain.Location

interface SaveFavoriteLocation : UseCase<Location, Long>