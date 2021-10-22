package co.feliperivera.mooveitworkshop.data

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.feliperivera.mooveitworkshop.MovieFactory
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import com.google.common.truth.Truth.*


@RunWith(AndroidJUnit4::class)
class MyDatabaseTest {
    private lateinit var movieDao: MovieDao
    private lateinit var remoteKeyDao: RemoteKeyDao
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
}
