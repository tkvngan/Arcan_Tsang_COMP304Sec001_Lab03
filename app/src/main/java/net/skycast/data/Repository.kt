package net.skycast.data

import net.skycast.domain.Location
import net.skycast.domain.WeatherInfo

interface Repository {

    suspend fun getWeatherRecord(id: Long): Pair<Location, WeatherInfo>?

    suspend fun getAllWeatherRecords(): Map<Long, Pair<Location, WeatherInfo>>

    suspend fun storeWeatherRecord(location: Location, weatherInfo: WeatherInfo): Long

    suspend fun deleteWeatherRecord(id: Long)

    suspend fun deleteAllWeatherRecords()

    suspend fun getFavoriteLocation(id: Long): Location?

    suspend fun getAllFavoriteLocations(): Map<Long, Location>

    suspend fun storeFavoriteLocation(location: Location): Long

    suspend fun deleteFavoriteLocation(id: Long)

    suspend fun deleteAllFavoriteLocations()

}