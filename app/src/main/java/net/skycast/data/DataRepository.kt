package net.skycast.data

import androidx.compose.ui.platform.WindowInfo
import net.skycast.domain.Location

interface DataRepository {

    suspend fun getWeatherRecord(id: Long): WeatherRecord?

    suspend fun getAllWeatherRecords(): List<WeatherRecord>

    suspend fun storeWeatherRecord(location: Location, weatherInfo: WindowInfo): WeatherRecord

    suspend fun deleteWeatherRecord(id: Long)

    suspend fun deleteAllWeatherRecords()

    suspend fun getFavoriteLocation(id: Long): FavoriteLocation?

    suspend fun getAllFavoriteLocations(): List<FavoriteLocation>

    suspend fun storeFavoriteLocation(location: Location): FavoriteLocation

    suspend fun deleteFavoriteLocation(id: Long)

    suspend fun deleteAllFavoriteLocations()

}