package net.skycast.domain


data class WeatherInfo(
    val timestamp: Long,
    val timeZone: String,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Double,
    val windSpeed: Double,
    val windDirection: String,
    val weatherConditions: String,
    val visibility: Double?,
    val cloudCover: Double?,
    val precipitation: Double?,
    val uvIndex: Double?,
    val airQualityIndex: Double?,
    val sunrise: String?,
    val sunset: String?,
    val snow: Double?,
    val dewPoint: Double?,
    val maxTemperature: Double?,
    val minTemperature: Double?,
    val maxFeelsLike: Double?,
    val minFeelsLike: Double?,
)
