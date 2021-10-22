package co.feliperivera.mooveitworkshop.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.lang.reflect.Array

@Entity
data class Movie (
    @PrimaryKey
    var id: Int = 0,
    val title: String,
    val release_date: String?,
    val poster_path: String?,
    val popularity: Float,
    val vote_average: Float,
    val overview: String?
){
    @Ignore var genres: List<MovieGenre>? = null
}