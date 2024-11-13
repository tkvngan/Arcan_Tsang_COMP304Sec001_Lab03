package net.skycast.application

import net.skycast.domain.WeatherData

interface LoadWeatherData : UseCase<Unit, Map<Long, WeatherData>>