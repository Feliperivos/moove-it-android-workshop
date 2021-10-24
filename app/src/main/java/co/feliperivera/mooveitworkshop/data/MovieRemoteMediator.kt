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
    private val webService: WebService,

): RemoteMediator<Int, Movie>() {

    val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
    val lastUpdatedKey = "last_updated"

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
                    var remoteKey: Int? = db.remoteKeyDao().remoteKeyByQuery(query)
                    if(remoteKey != null){
                        page = remoteKey
                    }
                }
            }
            val response = webService.getPopularMovies(page)
            if (loadType == LoadType.REFRESH) {
                db.movieDao().clearAll()
                db.movieDao().clearAllGenres()
                db.movieDao().clearAllGenresRelations()
                db.remoteKeyDao().deleteByQuery(query)
                val genresResponse = webService.getMovieGenres()
                db.movieDao().insertMovieGenres(genresResponse.genres)
            }
            if(response.results.isEmpty()){
                MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }else{
                db.remoteKeyDao().insertOrReplace(
                    RemoteKey(query, page + 1 )
                )

                val moviesGenresRelations = mutableListOf<MoviesGenresRelations>()
                response.results.forEach { movie ->
                    movie.genre_ids.forEach { genreId ->
                        moviesGenresRelations.add(MoviesGenresRelations(movie.id, genreId))
                    }
                    val videosResponse = webService.getMovieVideos(movie.id)
                    for(video in videosResponse.results){
                        if(video.site == "YouTube"){
                            movie.video_key = video.key
                            break
                        }
                    }
                }
                db.movieDao().insertAll(response.results)
                db.movieDao().insertMovieGenresRelations(moviesGenresRelations)
                db.stateDao().insertOrReplace(State(lastUpdatedKey, System.currentTimeMillis().toString()))

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
        val lastUpdated: String? = db.stateDao().stateByQuery(lastUpdatedKey)
        return if(lastUpdated != null && (System.currentTimeMillis() - lastUpdated.toLong()) <= cacheTimeout){
            InitializeAction.SKIP_INITIAL_REFRESH
        }else{
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }



}