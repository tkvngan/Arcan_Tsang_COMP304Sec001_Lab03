package net.skycast.infrastructure.room

import android.content.Context
import androidx.room.Room
import net.skycast.data.Repository
import net.skycast.domain.Location
import net.skycast.domain.WeatherData

class AppRepository(val context: Context) : Repository {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "skycast_database"
        ).build()
    }

    private val favoriteLocationDao by lazy {
        database.favoriteLocationDao()
    }

    private val weatherRecordDao by lazy {
        database.weatherRecordDao()
    }

    override suspend fun getWeatherRecord(id: Long): WeatherData? {
        return weatherRecordDao.getWeatherRecord(id)?.let { record ->
            WeatherData(record.location, current = record.weatherInfo, forecasts = emptyList())
        }
    }

    override suspend fun getAllWeatherRecords(): Map<Long, WeatherData> {
        return weatherRecordDao.getAllWeatherRecords().associate { record ->
            record.id to WeatherData(record.location, current = record.weatherInfo, forecasts = emptyList())
        }
    }

    override suspend fun addWeatherRecord(data: WeatherData): Long {
        return weatherRecordDao.insertWeatherRecord(
            WeatherRecord(id = 0, location = data.location, weatherInfo = data.current)
        )
    }

    override suspend fun deleteWeatherRecord(id: Long) {
        weatherRecordDao.deleteWeatherRecord(id)
    }

    override suspend fun deleteAllWeatherRecords() {
        weatherRecordDao.deleteAllWeatherRecords()
    }

    override suspend fun getFavoriteLocation(id: Long): Location? {
        return favoriteLocationDao.getFavoriteLocation(id)?.location
    }

    override suspend fun getAllFavoriteLocations(): Map<Long, Location> {
        return favoriteLocationDao.getAllFavoriteLocations().associate { (id, location) ->
            Pair(id, location)
        }
    }

    override suspend fun addFavoriteLocation(location: Location): Long {
        return favoriteLocationDao.insertFavoriteLocation(FavoriteLocation(0, location))
    }

    override suspend fun deleteFavoriteLocation(id: Long) {
        favoriteLocationDao.deleteFavoriteLocation(id)
    }

    override suspend fun deleteAllFavoriteLocations() {
        favoriteLocationDao.deleteAllFavoriteLocations()
    }
}