package net.skycast.infrastructure.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherRecordDao {

    @Insert
    suspend fun insertWeatherRecord(weatherRecord: WeatherRecord): Long

    @Query("DELETE FROM WeatherRecord WHERE id = :id")
    suspend fun deleteWeatherRecord(id: Long)

    @Query("DELETE FROM WeatherRecord")
    suspend fun deleteAllWeatherRecords()

    @Query("SELECT * FROM WeatherRecord WHERE id = :id")
    suspend fun getWeatherRecord(id: Long): WeatherRecord?

    @Query("SELECT * FROM WeatherRecord")
    suspend fun getAllWeatherRecords(): List<WeatherRecord>

}