package co.feliperivera.mooveitworkshop.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieWithGenres(
    @Embedded
    val data: Movie,
    @Relation(
        parentColumn = "id",
        entity = MovieGenre::class,
        entityColumn = "id",
        associateBy = Junction(
            MoviesGenresRelations::class,
            parentColumn = "movieId",
            entityColumn = "genreId")
    )
    val genres: List<MovieGenre>
)