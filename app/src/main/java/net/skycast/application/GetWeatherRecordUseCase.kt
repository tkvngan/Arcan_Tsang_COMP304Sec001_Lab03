package net.skycast.application

import net.skycast.domain.Location
import net.skycast.domain.WeatherInfo

interface GetWeatherRecordUseCase : UseCase<Long, Pair<Location, WeatherInfo>>