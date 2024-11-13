package net.skycast.application

import net.skycast.domain.Location
import net.skycast.domain.WeatherInfo

interface GetWeather : UseCase<GetWeather.Parameters, Pair<Location, WeatherInfo>?> {

    data class Parameters(
        val latitude: Double? = null,
        val longitude: Double? = null,
        val city: String? = null,
        val country: String? = null,
        val postalCode: String? = null
    )
}
