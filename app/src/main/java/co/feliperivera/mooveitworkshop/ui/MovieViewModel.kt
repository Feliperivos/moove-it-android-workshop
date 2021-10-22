package co.feliperivera.mooveitworkshop.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.*
import co.feliperivera.mooveitworkshop.data.Movie
import co.feliperivera.mooveitworkshop.data.MovieDao
import co.feliperivera.mooveitworkshop.data.MovieRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieDao: MovieDao,
    private val moviesMediator: MovieRemoteMediator
) : ViewModel() {

    @ExperimentalPagingApi
    var mostPopularMovies: LiveData<PagingData<Movie>> = Pager(
        PagingConfig( 20),

        null,  // initialKey
        moviesMediator,
        { movieDao.getMostPopularMovies() }
    ).liveData
}