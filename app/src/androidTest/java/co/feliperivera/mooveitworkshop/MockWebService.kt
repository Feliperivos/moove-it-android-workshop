package co.feliperivera.mooveitworkshop

import co.feliperivera.mooveitworkshop.data.entities.Movie
import co.feliperivera.mooveitworkshop.data.entities.MovieGenre
import co.feliperivera.mooveitworkshop.data.WebService
import co.feliperivera.mooveitworkshop.data.entities.Review
import java.io.IOException

class MockWebService: WebService {

    private val movies: MutableList<Movie> = mutableListOf<Movie>()
    private val genres: MutableList<MovieGenre> = mutableListOf<MovieGenre>()
    private val videos: MutableList<WebService.Videos> = mutableListOf<WebService.Videos>()
    private val reviews: MutableList<Review> = mutableListOf<Review>()

    var failureMsg: String? = null

    fun addMovies(movie: Movie){
        movies.add(movie)
    }

    fun addReviews(review: Review){
        reviews.add(review)
    }

    override suspend fun getPopularMovies(page: Int): WebService.BaseResponse<Movie> {
        failureMsg?.let {
            throw IOException(it)
        }
        return WebService.BaseResponse(movies)
    }

    override suspend fun getMovieGenres(): WebService.GenreResponse {
        failureMsg?.let {
            throw IOException(it)
        }
        return WebService.GenreResponse(genres)
    }

    override suspend fun getMovieVideos(movieId: Int): WebService.BaseResponse<WebService.Videos> {
        failureMsg?.let {
            throw IOException(it)
        }
        return WebService.BaseResponse(videos)
    }

    override suspend fun getMovieReviews(movieId: Int, page: Int): WebService.BaseResponse<Review> {
        failureMsg?.let {
            throw IOException(it)
        }
        return WebService.BaseResponse(reviews)
    }
}