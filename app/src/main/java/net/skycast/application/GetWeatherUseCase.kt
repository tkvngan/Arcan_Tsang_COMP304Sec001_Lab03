package net.skycast.application

import net.skycast.domain.Location
import net.skycast.domain.WeatherInfo


interface GetWeatherUseCase : UseCase<Location, Pair<Location, WeatherInfo>>
