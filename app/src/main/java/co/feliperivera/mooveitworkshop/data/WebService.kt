package co.feliperivera.mooveitworkshop.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {
    @GET("discover/movie?sort_by=popularity.desc")
    suspend fun getPopularMovies(@Query("page") page: Int) : BaseResponse<Movie>
}