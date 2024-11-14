package net.skycast.infrastructure.weatherbit

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class WeatherbitApi(
    val key: String,
    val units: Units? = null,
    val lang: Lang? = null
) {
    private val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }
    }

    suspend fun getCurrentObservations(
        key: String = this.key,
        lat: Double? = null,
        lon: Double? = null,
        cityId: String? = null,
        city: String? = null,
        postalCode: String? = null,
        country: String? = null,
        station: String? = null,
        stations: String? = null,
        points: String? = null,
        cities: String? = null,
        units: Units? = this.units,
        lang: Lang? = this.lang
    ): CurrentObservations {
        val response = httpClient.get("https://api.weatherbit.io/v2.0/current") {
            parameter("key", key)
            lat?.let { parameter("lat", it) }
            lon?.let { parameter("lon", it) }
            cityId?.let { parameter("city_id", it) }
            city?.let { parameter("city", it) }
            postalCode?.let { parameter("postal_code", it) }
            country?.let { parameter("country", it) }
            station?.let { parameter("station", it) }
            stations?.let { parameter("stations", it) }
            points?.let { parameter("points", it) }
            cities?.let { parameter("cities", it) }
            units?.let { parameter("units", it) }
            lang?.let { parameter("lang", it) }
        }
        if (response.status.value != 200) {
            println(response.status)
            throw WeatherbitException(response.status.value, response.status.description)
        }
        return response.body<CurrentObservations>()
    }

    suspend fun getDailyForecast(
        key: String = this.key,
        lat: Double? = null,
        lon: Double? = null,
        cityId: String? = null,
        city: String? = null,
        postalCode: String? = null,
        country: String? = null,
        station: String? = null,
        days: Int? = null,
        units: Units? = null,
        lang: Lang? = null
    ): ForecastDaily {
        val response = httpClient.get("https://api.weatherbit.io/v2.0/forecast/daily") {
            parameter("key", key)
            lat?.let { parameter("lat", it) }
            lon?.let { parameter("lon", it) }
            cityId?.let { parameter("city_id", it) }
            city?.let { parameter("city", it) }
            postalCode?.let { parameter("postal_code", it) }
            country?.let { parameter("country", it) }
            station?.let { parameter("station", it) }
            days?.let { parameter("days", it) }
            units?.let { parameter("units", it) }
            lang?.let { parameter("lang", it) }
        }
        if (response.status.value != 200) {
            println(response.status)
            throw WeatherbitException(response.status.value, response.status.description)
        }
        return response.body<ForecastDaily>()
    }

}

data class WeatherbitException(val code: Int, override val message: String) : Exception(message)