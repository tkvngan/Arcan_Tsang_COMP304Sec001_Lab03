package net.skycast.domain

import kotlinx.serialization.SerialName


data class WeatherInfo(
    val timestamp: Long,
    val temperature: Double,
    val feelsLike: Double? = null,
    val humidity: Double? = null,
    val windSpeed: Double? = null,
    val windDirection: String? = null,
    val weatherCode: String? = null,
    val weatherDescription: String? = null,
    val visibility: Double? = null,
    val cloudCover: Double? = null,
    val precipitation: Double? = null,
    val uvIndex: Double? = null,
    val airQualityIndex: Double? = null,
    val sunrise: String? = null,
    val sunset: String? = null,
    val snow: Double? = null,
    val dewPoint: Double? = null,
    val maxTemperature: Double? = null,
    val minTemperature: Double? = null,
    val maxFeelsLike: Double? = null,
    val minFeelsLike: Double? = null,
)
