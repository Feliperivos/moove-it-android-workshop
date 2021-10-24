package co.feliperivera.mooveitworkshop.data

import co.feliperivera.mooveitworkshop.data.entities.Movie
import co.feliperivera.mooveitworkshop.data.entities.MovieGenre
import co.feliperivera.mooveitworkshop.data.entities.Review
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {
    @GET("discover/movie?sort_by=popularity.desc")
    suspend fun getPopularMovies(@Query("page") page: Int) : BaseResponse<Movie>

    @GET("genre/movie/list")
    suspend fun getMovieGenres() : GenreResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(@Path("movie_id") movieId: Int): BaseResponse<Videos>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(@Path("movie_id") movieId: Int, @Query("page") page: Int): BaseResponse<Review>

    data class BaseResponse<T>(val results: List<T>, val total_results: Int? = null)
    data class GenreResponse(val genres: List<MovieGenre>)
    data class Videos(val site: String, val key: String )
}