package co.feliperivera.mooveitworkshop.data

import android.content.Context
import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import co.feliperivera.mooveitworkshop.MockWebService
import co.feliperivera.mooveitworkshop.MovieFactory
import co.feliperivera.mooveitworkshop.data.dao.MovieDao
import co.feliperivera.mooveitworkshop.data.dao.RemoteKeyDao
import co.feliperivera.mooveitworkshop.data.dao.StateDao
import co.feliperivera.mooveitworkshop.data.entities.Movie
import co.feliperivera.mooveitworkshop.data.entities.Review
import co.feliperivera.mooveitworkshop.data.remotemediators.MovieRemoteMediator
import co.feliperivera.mooveitworkshop.data.remotemediators.ReviewRemoteMediator
import com.google.common.truth.Truth
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
class ReviewRemoteMediatorTest{

    private lateinit var db: MyDatabase
    private lateinit var mockWebService: MockWebService

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
        mockWebService.addReviews(Review("1","name","content", "createDate"))
        val remoteMediator = ReviewRemoteMediator(1,db,mockWebService)
        val pagingState = PagingState<Int, Review>(
            listOf(),
            null,
            PagingConfig(1),
            0
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Truth.assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        Truth.assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isFalse()
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runBlocking {
        val remoteMediator = ReviewRemoteMediator(1,db,mockWebService)
        val pagingState = PagingState<Int, Review>(
            listOf(),
            null,
            PagingConfig(1),
            0
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Truth.assertThat(result is RemoteMediator.MediatorResult.Success).isTrue()
        Truth.assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached).isTrue()
    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runBlocking {
        mockWebService.failureMsg = "Throw test failure"
        val remoteMediator = ReviewRemoteMediator(1,db,mockWebService)
        val pagingState = PagingState<Int, Review>(
            listOf(),
            null,
            PagingConfig(1),
            0
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        Truth.assertThat(result is RemoteMediator.MediatorResult.Error).isTrue()
    }
}