package co.feliperivera.mooveitworkshop.data

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.feliperivera.mooveitworkshop.MovieFactory
import co.feliperivera.mooveitworkshop.data.dao.MovieDao
import co.feliperivera.mooveitworkshop.data.dao.RemoteKeyDao
import co.feliperivera.mooveitworkshop.data.dao.ReviewDao
import co.feliperivera.mooveitworkshop.data.dao.StateDao
import co.feliperivera.mooveitworkshop.data.entities.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class MyDatabaseTest {
    private lateinit var movieDao: MovieDao
    private lateinit var remoteKeyDao: RemoteKeyDao
    private lateinit var stateDao: StateDao
    private lateinit var reviewDao: ReviewDao
    private lateinit var db: MyDatabase

    private lateinit var movieFactory: MovieFactory
    private lateinit var mockMovies: MutableList<Movie>

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MyDatabase::class.java).build()
        movieDao = db.movieDao()
        remoteKeyDao = db.remoteKeyDao()
        stateDao = db.stateDao()
        reviewDao = db.reviewDao()
        movieFactory = MovieFactory()
        mockMovies = mutableListOf<Movie>()
        mockMovies.add(movieFactory.createMovie())

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeMoviesAndRead() = runBlocking {
        movieDao.insertAll(mockMovies)
        var movies = movieDao.getMostPopularMovies().load(
            PagingSource.LoadParams.Refresh(0,1,false)
        )
        var expected = PagingSource.LoadResult.Page(
            mockMovies,null, null,0,0
        )
        assertThat(movies).isEqualTo(expected)
    }

    @Test
    @Throws(Exception::class)
    fun writeMoviesAndDelete() = runBlocking {
        movieDao.insertAll(mockMovies)
        var movies = movieDao.getMostPopularMovies().load(
            PagingSource.LoadParams.Refresh(0,1,false)
        )
        var expected = PagingSource.LoadResult.Page(
            mockMovies,null, null,0,0
        )
        assertThat(movies).isEqualTo(expected)

        // Test clearing table

        movieDao.clearAll()
        movies = movieDao.getMostPopularMovies().load(
            PagingSource.LoadParams.Refresh(0,1,false)
        )
        expected = PagingSource.LoadResult.Page(
            mutableListOf<Movie>(),null, null,0,0
        )
        assertThat(movies).isEqualTo(expected)
    }

    @Test
    @Throws(Exception::class)
    fun writeRemoteKeyAndRead() = runBlocking {
        val query = "movie"
        val remoteKey = RemoteKey(query, 2)
        remoteKeyDao.insertOrReplace(remoteKey)
        val result = remoteKeyDao.remoteKeyByQuery(query)
        assertThat(result).isEqualTo(remoteKey.nextKey)
    }

    @Test
    @Throws(Exception::class)
    fun writeRemoteKeyAndDelete() = runBlocking {
        val query = "movie"
        val remoteKey = RemoteKey(query, 2)
        remoteKeyDao.insertOrReplace(remoteKey)
        val result = remoteKeyDao.remoteKeyByQuery(query)
        assertThat(result).isEqualTo(remoteKey.nextKey)

        remoteKeyDao.deleteByQuery(query)
        val emptyResult: Int? = remoteKeyDao.remoteKeyByQuery(query)
        //System.out.println(emptyResult)
        assertThat(emptyResult).isNull()
    }

    @Test
    @Throws(Exception::class)
    fun writeMovieGenreRelationAndRead() = runBlocking {
        val genres = listOf(MovieGenre(1, "Drama"))
        movieDao.insertMovieGenres(genres)
        movieDao.insertAll(mockMovies)
        val movieGenresRelations = listOf(MoviesGenresRelations(mockMovies[0].id, genres[0].id))
        movieDao.insertMovieGenresRelations(movieGenresRelations)
        val result = movieDao.getMovieWithGenres(mockMovies[0].id)
        assertThat(result.genres[0].name).isEqualTo(genres[0].name)
    }

    @Test
    @Throws(Exception::class)
    fun writeMovieGenreRelationAndDelete() = runBlocking {
        val genres = listOf(MovieGenre(1, "Drama"))
        movieDao.insertMovieGenres(genres)
        movieDao.insertAll(mockMovies)
        val movieGenresRelations = listOf(MoviesGenresRelations(mockMovies[0].id, genres[0].id))
        movieDao.insertMovieGenresRelations(movieGenresRelations)
        val result = movieDao.getMovieWithGenres(mockMovies[0].id)
        assertThat(result.genres[0].name).isEqualTo(genres[0].name)

        movieDao.clearAllGenresRelations()
        val movieWithNoGenres = movieDao.getMovieWithGenres(mockMovies[0].id)
        assertThat(movieWithNoGenres.genres).isEmpty()
    }

    @Test
    @Throws(Exception::class)
    fun writeStateAndRead() = runBlocking {
        val state = State("key", "value")
        stateDao.insertOrReplace(state)
        val result = stateDao.stateByQuery(state.state_key)
        assertThat(result).isEqualTo(state.value)
    }

    @Test
    @Throws(Exception::class)
    fun writeStateAndDelete() = runBlocking {
        val state = State("key", "value")
        stateDao.insertOrReplace(state)
        val result = stateDao.stateByQuery(state.state_key)
        assertThat(result).isEqualTo(state.value)

        stateDao.deleteByQuery(state.state_key)
        val emptyResult: String? = stateDao.stateByQuery(state.state_key)
        assertThat(emptyResult).isNull()
    }

    @Test
    @Throws(Exception::class)
    fun writeReviewAndRead() = runBlocking {

        val reviews = listOf(Review("1","name","content", "createDate"))
        reviewDao.insertAllReviews(reviews)
        val results = reviewDao.getReviews().load(
            PagingSource.LoadParams.Refresh(0,1,false)
        )
        var expected = PagingSource.LoadResult.Page(
            reviews,null, null,0,0
        )
        assertThat(results).isEqualTo(expected)
    }

    @Test
    @Throws(Exception::class)
    fun writeReviewAndDelete() = runBlocking {
        val reviews = listOf(Review("1","name","content", "createDate"))
        reviewDao.insertAllReviews(reviews)
        var results = reviewDao.getReviews().load(
            PagingSource.LoadParams.Refresh(0,1,false)
        )
        var expected = PagingSource.LoadResult.Page(
            reviews,null, null,0,0
        )
        assertThat(results).isEqualTo(expected)

        reviewDao.clearAllReviews()
        results = reviewDao.getReviews().load(
            PagingSource.LoadParams.Refresh(0,1,false)
        )
        expected = PagingSource.LoadResult.Page(
            mutableListOf<Review>(),null, null,0,0
        )
        assertThat(results).isEqualTo(expected)
    }
}
