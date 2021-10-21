package co.feliperivera.mooveitworkshop.data

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(movie: Movie)

    @Query("SELECT * FROM movie ORDER BY popularity DESC")
    fun getMostPopularMovies(): PagingSource<Int, Movie>

    @Query("DELETE FROM movie")
    suspend fun clearAll()
}