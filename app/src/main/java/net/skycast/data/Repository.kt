package net.skycast.data

import net.skycast.domain.Location
import net.skycast.domain.WeatherData
import net.skycast.domain.WeatherInfo

interface Repository {

    suspend fun getWeatherRecord(id: Long): WeatherData?

    suspend fun getAllWeatherRecords(): Map<Long, WeatherData>

    suspend fun addWeatherRecord(data: WeatherData): Long

    suspend fun deleteWeatherRecord(id: Long)

    suspend fun deleteAllWeatherRecords()


    suspend fun getFavoriteLocation(id: Long): Location?

    suspend fun getAllFavoriteLocations(): Map<Long, Location>

    suspend fun addFavoriteLocation(location: Location): Long

    suspend fun deleteFavoriteLocation(id: Long)

    suspend fun deleteAllFavoriteLocations()

}