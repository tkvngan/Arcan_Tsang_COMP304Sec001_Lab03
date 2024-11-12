package net.skycast.application

import net.skycast.domain.Location
import net.skycast.domain.WeatherInfo

interface GetAllWeatherRecordsUseCase : UseCase<Unit, List<Pair<Location, WeatherInfo>>>