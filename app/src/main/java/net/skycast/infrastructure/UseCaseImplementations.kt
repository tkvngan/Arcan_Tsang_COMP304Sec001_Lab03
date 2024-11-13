@file:Suppress("PropertyName")

package net.skycast.infrastructure

import net.skycast.application.GetWeatherData
import net.skycast.application.LoadFavoriteLocations
import net.skycast.application.LoadWeatherData
import net.skycast.application.RemoveFavoriteLocation
import net.skycast.application.RemoveWeatherData
import net.skycast.application.SaveFavoriteLocation
import net.skycast.application.SaveWeatherData
import net.skycast.application.WeatherParameters
import net.skycast.domain.Location
import net.skycast.domain.PartOfDay
import net.skycast.domain.WeatherData
import net.skycast.domain.WeatherInfo
import net.skycast.infrastructure.room.AppRepository
import net.skycast.infrastructure.weatherbit.CurrentObservation
import net.skycast.infrastructure.weatherbit.Forecast
import net.skycast.infrastructure.weatherbit.WeatherbitApi

class UseCaseImplementations(
    val repository: AppRepository,
    val api: WeatherbitApi
) {
    val GetWeatherData: GetWeatherData = object : GetWeatherData {
        override suspend fun invoke(parameters: WeatherParameters): WeatherData {
            val observation = api.getCurrentObservations(
                    lat = parameters.latitude,
                    lon = parameters.longitude,
                    city = parameters.city,
                    postalCode = parameters.postalCode,
                    country = parameters.country,
                ).data?.firstOrNull() ?: throw Exception("Failed to get current weather data")
            val location = observation.toLocation()
            val current = observation.toWeatherInfo()
            val forecasts = api.getDailyForecast(
                    lat = parameters.latitude,
                    lon = parameters.longitude,
                    city = parameters.city,
                    postalCode = parameters.postalCode,
                    country = parameters.country,
                    days = parameters.days,
                ).data?.map { it.toWeatherInfo() } ?: emptyList()
            return WeatherData(location, current, forecasts)
        }
    }

    val LoadFavoriteLocations: LoadFavoriteLocations = object : LoadFavoriteLocations {
        override suspend fun invoke(input: Unit): Map<Long, Location> {
            return repository.getAllFavoriteLocations()
        }
    }

    val LoadWeatherData: LoadWeatherData = object : LoadWeatherData {
        override suspend fun invoke(input: Unit): Map<Long, WeatherData> {
            return repository.getAllWeatherRecords()
        }
    }

    val RemoveFavoriteLocation: RemoveFavoriteLocation = object : RemoveFavoriteLocation {
        override suspend fun invoke(id: Long) {
            repository.deleteFavoriteLocation(id)
        }
    }

    val RemoveWeatherData: RemoveWeatherData = object : RemoveWeatherData {
        override suspend fun invoke(id: Long) {
            repository.deleteWeatherRecord(id)
        }
    }

    val SaveFavoriteLocation: SaveFavoriteLocation = object : SaveFavoriteLocation {
        override suspend fun invoke(location: Location): Long {
            return repository.addFavoriteLocation(location)
        }
    }

    val SaveWeatherData: SaveWeatherData = object : SaveWeatherData {
        override suspend fun invoke(weatherData: WeatherData): Long {
            return repository.addWeatherRecord(weatherData)
        }
    }
}

fun CurrentObservation.toLocation(): Location {
    return Location(
        latitude = lat!!,
        longitude = lon!!,
        cityName = cityName,
        stateCode = stateCode,
        countryCode = countryCode,
        timezone = timezone,
    )
}

fun CurrentObservation.toWeatherInfo(): WeatherInfo {
    return WeatherInfo(
        timestamp = ts!!,
        temperature = temp!!,
        feelsLike = appTemp,
        humidity = rh?.toDouble(),
        windSpeed = windSpeed,
        windDirection = windCdir,
        weatherCode = weather?.code,
        weatherDescription = weather?.description,
        visibility = vis?.toDouble(),
        cloudCover = clouds?.toDouble(),
        precipitation = precip,
        pressure = pres,
        uvIndex = uv,
        airQualityIndex = aqi,
        sunrise = sunrise,
        sunset = sunset,
        snow = snow,
        dewPoint = dewpt,
        partOfDay = when (pod) {
            "d" -> PartOfDay.DAY
            "n" -> PartOfDay.NIGHT
            else -> null
        },
    )
}

fun Forecast.toWeatherInfo(): WeatherInfo {
    return WeatherInfo(
        timestamp = ts!!,
        temperature = temp!!,
        humidity = rh?.toDouble(),
        windSpeed = windSpd,
        windDirection = windCdir,
        weatherCode = weather?.code,
        weatherDescription = weather?.description,
        visibility = vis?.toDouble(),
        cloudCover = clouds?.toDouble(),
        precipitation = precip,
        pressure = pres,
        uvIndex = uv,
        sunrise = sunriseTs.toString(),
        sunset = sunsetTs.toString(),
        snow = snow,
        dewPoint = dewpt,
        partOfDay = when (pod) {
            "d" -> PartOfDay.DAY
            "n" -> PartOfDay.NIGHT
            else -> null
        },
        maxTemperature = maxTemp,
        minTemperature = minTemp,
        maxFeelsLike = appMaxTemp,
        minFeelsLike = appMinTemp,
    )
}
