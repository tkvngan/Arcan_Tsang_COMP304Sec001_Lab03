package net.skycast.application

import net.skycast.domain.Location
import net.skycast.domain.WeatherInfo

interface GetWeatherForecast : UseCase<GetWeatherForecast.Parameters, Pair<Location, List<WeatherInfo>>?> {

    data class Parameters(
        val latitude: Double? = null,
        val longitude: Double? = null,
        val city: String? = null,
        val country: String? = null,
        val postalCode: String? = null,
        val days: Int = 7
    )
}
