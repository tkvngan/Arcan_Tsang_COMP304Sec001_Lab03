package net.skycast.infrastructure.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteLocationDao {

    @Insert
    suspend fun insertFavoriteLocation(location: FavoriteLocation): Long

    @Query("DELETE FROM FavoriteLocation WHERE id = :id")
    suspend fun deleteFavoriteLocation(id: Long)

    @Query("DELETE FROM FavoriteLocation")
    suspend fun deleteAllFavoriteLocations()

    @Query("SELECT * FROM FavoriteLocation WHERE id = :id")
    suspend fun getFavoriteLocation(id: Long): FavoriteLocation?

    @Query("SELECT * FROM FavoriteLocation")
    suspend fun getAllFavoriteLocations(): List<FavoriteLocation>


}