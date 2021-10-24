package co.feliperivera.mooveitworkshop.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_genre")
data class MovieGenre(
    @PrimaryKey
    val id: Int,
    val name: String
)
