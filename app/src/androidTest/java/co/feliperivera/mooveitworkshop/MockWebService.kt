package co.feliperivera.mooveitworkshop

import co.feliperivera.mooveitworkshop.data.Movie
import co.feliperivera.mooveitworkshop.data.MovieGenre
import co.feliperivera.mooveitworkshop.data.WebService
import java.io.IOException

class MockWebService: WebService {

    private val movies: MutableList<Movie> = mutableListOf<Movie>()
    private val genres: MutableList<MovieGenre> = mutableListOf<MovieGenre>()
    private val videos: MutableList<WebService.Videos> = mutableListOf<WebService.Videos>()

    var failureMsg: String? = null

    fun addMovies(movie: Movie){
        movies.add(movie)
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
        System.out.println("genres")
        return WebService.GenreResponse(genres)
    }

    override suspend fun getMovieVideos(movieId: Int): WebService.BaseResponse<WebService.Videos> {
        failureMsg?.let {
            throw IOException(it)
        }
        System.out.println("videos")
        return WebService.BaseResponse(videos)
    }
}