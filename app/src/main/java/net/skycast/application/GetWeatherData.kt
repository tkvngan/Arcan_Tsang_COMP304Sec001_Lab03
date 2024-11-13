package net.skycast.application

import net.skycast.domain.WeatherData

data class WeatherParameters(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val city: String? = null,
    val country: String? = null,
    val postalCode: String? = null,
    val days: Int = 7,
)

interface GetWeatherData : UseCase<WeatherParameters, WeatherData>