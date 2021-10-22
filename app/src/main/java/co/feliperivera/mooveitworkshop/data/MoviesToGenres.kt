package co.feliperivera.mooveitworkshop.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_genres_relation")
data class MoviesToGenres(
    @PrimaryKey
    var id: Int = 0,
    val movieId: Int,
    val genreId: Int
)
