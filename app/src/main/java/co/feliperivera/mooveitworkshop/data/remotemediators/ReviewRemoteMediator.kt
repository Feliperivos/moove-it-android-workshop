package co.feliperivera.mooveitworkshop.data.remotemediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import co.feliperivera.mooveitworkshop.data.MyDatabase
import co.feliperivera.mooveitworkshop.data.WebService
import co.feliperivera.mooveitworkshop.data.entities.RemoteKey
import co.feliperivera.mooveitworkshop.data.entities.Review
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ReviewRemoteMediator @Inject constructor(
    private val movieId: Int,
    private val db: MyDatabase,
    private val webService: WebService,

    ): RemoteMediator<Int, Review>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Review>
    ): MediatorResult {
        val query = "review"
        return try {
            var page = 0
            when (loadType) {
                LoadType.REFRESH -> page = 1
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    var remoteKey: Int? = db.remoteKeyDao().remoteKeyByQuery(query)
                    if(remoteKey != null){
                        page = remoteKey
                    }
                }
            }
            val response = webService.getMovieReviews(movieId,page)
            if (loadType == LoadType.REFRESH) {
                db.reviewDao().clearAllReviews()
                db.remoteKeyDao().deleteByQuery(query)
            }
            if(response.results.isEmpty()){
                MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }else{
                db.remoteKeyDao().insertOrReplace(
                    RemoteKey(query, page + 1 )
                )
                response.results.forEach { review ->
                    review.author_avatar = review.author_details?.avatar_path
                }
                db.movieDao().saveNumberOfReviews(movieId,response.total_results)
                db.reviewDao().insertAllReviews(response.results)

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

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
}