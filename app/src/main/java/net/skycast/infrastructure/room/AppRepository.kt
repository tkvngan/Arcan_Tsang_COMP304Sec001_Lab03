package net.skycast.infrastructure.room

import android.content.Context
import androidx.room.Room
import net.skycast.data.Repository
import net.skycast.domain.Location
import net.skycast.domain.WeatherInfo

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

    override suspend fun getWeatherRecord(id: Long): Pair<Location, WeatherInfo>? {
        val weatherRecord = weatherRecordDao.getWeatherRecord(id)
        return weatherRecord?.let {
            Pair<Location, WeatherInfo>(it.location, it.weatherInfo)
        }
    }

    override suspend fun getAllWeatherRecords(): Map<Long, Pair<Location, WeatherInfo>> {
        return weatherRecordDao.getAllWeatherRecords().associate { record ->
            Pair(record.id, Pair(record.location, record.weatherInfo))
        }
    }

    override suspend fun storeWeatherRecord(location: Location, weatherInfo: WeatherInfo): Long {
        return weatherRecordDao.insertWeatherRecord(
            WeatherRecord(id = 0, location = location, weatherInfo = weatherInfo)
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

    override suspend fun storeFavoriteLocation(location: Location): Long {
        return favoriteLocationDao.insertFavoriteLocation(FavoriteLocation(0, location))
    }

    override suspend fun deleteFavoriteLocation(id: Long) {
        favoriteLocationDao.deleteFavoriteLocation(id)
    }

    override suspend fun deleteAllFavoriteLocations() {
        favoriteLocationDao.deleteAllFavoriteLocations()
    }
}