package net.skycast.infrastructure.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherRecord::class, FavoriteLocation::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteLocationDao(): FavoriteLocationDao
    abstract fun weatherRecordDao(): WeatherRecordDao
}
