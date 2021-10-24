package co.feliperivera.mooveitworkshop.data.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import co.feliperivera.mooveitworkshop.data.entities.Movie
import co.feliperivera.mooveitworkshop.data.entities.MovieGenre
import co.feliperivera.mooveitworkshop.data.entities.MoviesGenresRelations

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