package co.feliperivera.mooveitworkshop.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {
    @GET("discover/movie?sort_by=popularity.desc")
    suspend fun getPopularMovies(@Query("page") page: Int) : BaseResponse<Movie>

    @GET("genre/movie/list")
    suspend fun getMovieGenres() : GenreResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") id: Int) : Movie

    data class BaseResponse<T>(val results: List<T>)
    data class GenreResponse(val genres: List<MovieGenre>)
}