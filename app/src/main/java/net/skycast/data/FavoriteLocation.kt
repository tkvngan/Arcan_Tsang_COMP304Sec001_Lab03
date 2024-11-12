package net.skycast.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.skycast.domain.Location

@Entity
data class FavoriteLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @Embedded
    val location: Location
)
