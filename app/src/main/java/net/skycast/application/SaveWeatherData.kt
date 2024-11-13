package net.skycast.application

import net.skycast.domain.WeatherData

interface SaveWeatherData : UseCase<WeatherData, Long>

