package net.skycast.application

import net.skycast.domain.Location
import net.skycast.domain.WeatherInfo

interface LoadWeatherRecords : UseCase<Unit, Map<Long, Pair<Location, WeatherInfo>>>