package net.skycast.application

import net.skycast.domain.Location
import net.skycast.domain.WeatherInfo


interface GetWeatherForecastUseCase : UseCase<Location, Pair<Location, List<WeatherInfo>>>
