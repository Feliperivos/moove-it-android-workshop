package co.feliperivera.mooveitworkshop.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey
    var id: Int = 0,
    val title: String,
    val release_date: String?,
    val poster_path: String?,
    val popularity: Float,
    val vote_average: Float,
)