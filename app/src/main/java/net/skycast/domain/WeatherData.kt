package net.skycast.domain

data class WeatherData(
    val location: Location,
    val current: WeatherInfo,
    val forecasts: List<WeatherInfo>
)