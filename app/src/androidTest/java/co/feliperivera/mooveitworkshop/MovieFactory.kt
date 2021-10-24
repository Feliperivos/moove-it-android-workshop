package co.feliperivera.mooveitworkshop

import co.feliperivera.mooveitworkshop.data.entities.Movie
import java.util.concurrent.atomic.AtomicInteger

class MovieFactory {
    private val counter = AtomicInteger(0)
    fun createMovie(): Movie {
        val id = counter.incrementAndGet()
        return Movie(
            id = id,
            title = "title $id",
            release_date = "2008-12-$id",
            poster_path = "",
            popularity = id.toFloat() * (-1),
            vote_average = 9.8f,
            overview = "Overview",
            backdrop_path = null
        )
    }
}