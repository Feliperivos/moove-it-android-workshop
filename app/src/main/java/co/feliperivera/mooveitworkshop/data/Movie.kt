package co.feliperivera.mooveitworkshop.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
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
    val overview: String?,
    val backdrop_path: String?
){
    @Ignore
    var genre_ids: List<Int> = listOf<Int>()
    var video_key: String? = null
}