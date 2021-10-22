package co.feliperivera.mooveitworkshop

import co.feliperivera.mooveitworkshop.data.BaseResponse
import co.feliperivera.mooveitworkshop.data.Movie
import co.feliperivera.mooveitworkshop.data.WebService
import java.io.IOException

class MockWebService: WebService {

    private val movies: MutableList<Movie> = mutableListOf<Movie>()

    var failureMsg: String? = null

    fun addMovies(movie: Movie){
        movies.add(movie)
    }

    override suspend fun getPopularMovies(page: Int): BaseResponse<Movie> {
        failureMsg?.let {
            throw IOException(it)
        }
        return BaseResponse(movies)
    }
}