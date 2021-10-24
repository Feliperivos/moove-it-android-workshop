package co.feliperivera.mooveitworkshop.data.dao

import androidx.paging.PagingSource
import androidx.room.*
import co.feliperivera.mooveitworkshop.data.entities.Movie
import co.feliperivera.mooveitworkshop.data.entities.MovieWithGenres
import co.feliperivera.mooveitworkshop.data.entities.MoviesGenresRelations
import co.feliperivera.mooveitworkshop.data.entities.MovieGenre

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(movie: Movie)

    @Query("SELECT * FROM movie ORDER BY popularity DESC")
    fun getMostPopularMovies(): PagingSource<Int, Movie>

    @Transaction
    @Query("SELECT * FROM movie WHERE id = :id")
    suspend fun getMovieWithGenres(id: Int? = 0): MovieWithGenres

    @Query("DELETE FROM movie")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieGenres(genres: List<MovieGenre>)

    @Query("DELETE FROM movie_genre")
    suspend fun clearAllGenres()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieGenresRelations(genres: List<MoviesGenresRelations>)

    @Query("DELETE FROM movies_genres_relation")
    suspend fun clearAllGenresRelations()

    @Query("UPDATE movie SET reviews = :reviews WHERE id = :id")
    suspend fun saveNumberOfReviews(id: Int? = 0, reviews: Int? = 0)

}