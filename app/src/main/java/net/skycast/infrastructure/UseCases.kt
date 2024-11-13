@file:Suppress("PropertyName")

package net.skycast.infrastructure

import net.skycast.application.LoadFavoriteLocations
import net.skycast.application.LoadWeatherRecords
import net.skycast.application.GetWeather
import net.skycast.application.GetWeatherForecast
import net.skycast.application.RemoveFavoriteLocation
import net.skycast.application.RemoveWeatherRecord
import net.skycast.application.StoreFavoriteLocation
import net.skycast.application.StoreWeatherRecord
import net.skycast.domain.Location
import net.skycast.domain.WeatherInfo
import net.skycast.infrastructure.room.AppRepository
import net.skycast.infrastructure.weatherbit.WeatherbitApi

class UseCases(
    val repository: AppRepository,
    val api: WeatherbitApi) {

    val GetWeather: GetWeather = object : GetWeather {
        override suspend fun invoke(parameters: GetWeather.Parameters): Pair<Location, WeatherInfo>? {
            val observation = try {
                api.getCurrentObservations(
                    lat = parameters.latitude,
                    lon = parameters.longitude,
                    city = parameters.city,
                    postalCode = parameters.postalCode,
                    country = parameters.country,
                ).data?.firstOrNull()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } ?: return null
            val location = Location(
                latitude = observation.lat!!,
                longitude = observation.lon!!,
                cityName = observation.cityName,
                stateCode = observation.stateCode,
                countryCode = observation.countryCode,
                timezone = observation.timezone,
            )
            val weatherInfo = WeatherInfo(
                timestamp = observation.ts!!,
                temperature = observation.temp!!,
                feelsLike = observation.appTemp,
                humidity = observation.rh?.toDouble(),
                windSpeed = observation.windSpeed,
                windDirection = observation.windCdir,
                weatherCode = observation.weather?.code,
                weatherDescription = observation.weather?.description,
                visibility = observation.vis?.toDouble(),
                cloudCover = observation.clouds?.toDouble(),
                precipitation = observation.precip,
                uvIndex = observation.uv,
                airQualityIndex = observation.aqi,
                sunrise = observation.sunrise,
                sunset = observation.sunset,
                snow = observation.snow,
                dewPoint = observation.dewpt,
            )
            return Pair(location, weatherInfo)
        }
    }


    val GetWeatherForecast: GetWeatherForecast = object : GetWeatherForecast {
        override suspend fun invoke(parameters: GetWeatherForecast.Parameters): Pair<Location, List<WeatherInfo>>? {
            val forecasts = try {
                api.getDailyForecast(
                    lat = parameters.latitude,
                    lon = parameters.longitude,
                    city = parameters.city,
                    postalCode = parameters.postalCode,
                    country = parameters.country,
                    days = parameters.days,
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            } ?: return null

            val location = Location(
                latitude = forecasts.lat!!,
                longitude = forecasts.lon!!,
                cityName = forecasts.cityName,
                stateCode = forecasts.stateCode,
                countryCode = forecasts.countryCode,
                timezone = forecasts.timezone,
            )
            val weatherInfos = forecasts.data?.map { forecast ->
                WeatherInfo(
                    timestamp = forecast.ts!!,
                    temperature = forecast.temp!!,
                    humidity = forecast.rh?.toDouble(),
                    windSpeed = forecast.windSpd,
                    windDirection = forecast.windCdir,
                    weatherCode = forecast.weather?.code,
                    weatherDescription = forecast.weather?.description,
                    visibility = forecast.vis?.toDouble(),
                    cloudCover = forecast.clouds?.toDouble(),
                    precipitation = forecast.precip,
                    uvIndex = forecast.uv,
                    sunrise = forecast.sunriseTs.toString(),
                    sunset = forecast.sunsetTs.toString(),
                    snow = forecast.snow,
                    dewPoint = forecast.dewpt,
                    maxTemperature = forecast.maxTemp,
                    minTemperature = forecast.minTemp,
                    maxFeelsLike = forecast.appMaxTemp,
                    minFeelsLike = forecast.appMinTemp,
                )
            }
            return Pair(location, weatherInfos ?: emptyList())
        }
    }

    val LoadFavoriteLocations: LoadFavoriteLocations = object : LoadFavoriteLocations {
        override suspend fun invoke(input: Unit): Map<Long, Location> {
            return repository.getAllFavoriteLocations()
        }
    }

    val LoadWeatherRecords: LoadWeatherRecords = object : LoadWeatherRecords {
        override suspend fun invoke(input: Unit): Map<Long, Pair<Location, WeatherInfo>> {
            return repository.getAllWeatherRecords()
        }
    }

    val RemoveFavoriteLocation: RemoveFavoriteLocation = object : RemoveFavoriteLocation {
        override suspend fun invoke(id: Long) {
            repository.deleteFavoriteLocation(id)
        }
    }

    val RemoveWeatherRecord: RemoveWeatherRecord = object : RemoveWeatherRecord {
        override suspend fun invoke(id: Long) {
            repository.deleteWeatherRecord(id)
        }
    }

    val StoreFavoriteLocation: StoreFavoriteLocation = object : StoreFavoriteLocation {
        override suspend fun invoke(location: Location): Long {
            return repository.storeFavoriteLocation(location)
        }
    }

    val StoreWeatherRecord: StoreWeatherRecord = object : StoreWeatherRecord {
        override suspend fun invoke(input: Pair<Location, WeatherInfo>): Long {
            val (location, weatherInfo) = input
            return repository.storeWeatherRecord(location, weatherInfo)
        }
    }
}
