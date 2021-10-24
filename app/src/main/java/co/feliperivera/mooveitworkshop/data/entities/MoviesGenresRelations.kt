package co.feliperivera.mooveitworkshop.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "movies_genres_relation", primaryKeys = ["movieId", "genreId"])
data class MoviesGenresRelations(
    val movieId: Int,
    val genreId: Int
)
