package co.feliperivera.mooveitworkshop.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Database
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @Inject constructor(
    private val db: MyDatabase,
    private val movieDao: MovieDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val webService: WebService

): RemoteMediator<Int, Movie>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        val query = "movie"
        return try {
            var page = 0
            when (loadType) {
                LoadType.REFRESH -> page = 1
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    var remoteKey: Int? = remoteKeyDao.remoteKeyByQuery(query)
                    if(remoteKey != null){
                        page = remoteKey
                    }
                }
            }
            val response = webService.getPopularMovies(page)
            if (loadType == LoadType.REFRESH) {
                movieDao.clearAll()
                remoteKeyDao.deleteByQuery(query)
            }
            if(response.results.isEmpty()){
                MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }else{
                remoteKeyDao.insertOrReplace(
                    RemoteKey(query, page + 1 )
                )

                movieDao.insertAll(response.results)

                MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }

    }

    /*override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return if (System.currentTimeMillis() - db.lastUpdated() >= cacheTimeout)
        {
            // Cached data is up-to-date, so there is no need to re-fetch
            // from the network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network; returning
            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
            // APPEND and PREPEND from running until REFRESH succeeds.
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }*/



}