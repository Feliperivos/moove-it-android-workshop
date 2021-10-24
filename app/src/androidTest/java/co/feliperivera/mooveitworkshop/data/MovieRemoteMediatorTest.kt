package co.feliperivera.mooveitworkshop.data

import android.content.Context
import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.feliperivera.mooveitworkshop.MockWebService
import co.feliperivera.mooveitworkshop.MovieFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class MovieRemoteMediatorTest{

    private lateinit var movieDao: MovieDao
    private lateinit var remoteKeyDao: RemoteKeyDao
    private lateinit var db: MyDatabase
    private lateinit var mockWebService: MockWebService
    private lateinit var stateDao: StateDao

    @Before
    fun initialize() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MyDatabase::class.java).build()
        mockWebService = MockWebService()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runBlocking {
        val movieFactory = MovieFactory()
        mockWebService.addMovies(movieFactory.createMovie())
        val remoteMediator = MovieRemoteMediator(db,mockWebService)
        val pagingState = PagingState<Int, Movie>(
            listOf(),
            null,
            PagingConfig(1),
            0
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isFalse()
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runBlocking {
        val remoteMediator = MovieRemoteMediator(db,mockWebService)
        val pagingState = PagingState<Int, Movie>(
            listOf(),
            null,
            PagingConfig(1),
            0
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runBlocking {
        mockWebService.failureMsg = "Throw test failure"
        val remoteMediator = MovieRemoteMediator(db,mockWebService)
        val pagingState = PagingState<Int, Movie>(
            listOf(),
            null,
            PagingConfig(1),
            0
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertThat(result is RemoteMediator.MediatorResult.Error).isTrue()
    }
}
