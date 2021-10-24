package co.feliperivera.mooveitworkshop.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import co.feliperivera.mooveitworkshop.data.Movie
import co.feliperivera.mooveitworkshop.data.MovieDao
import co.feliperivera.mooveitworkshop.data.MovieRemoteMediator
import co.feliperivera.mooveitworkshop.data.MovieWithGenres
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieDao: MovieDao,
    private val moviesMediator: MovieRemoteMediator
) : ViewModel() {

    val movieId: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val currentMovie: MutableLiveData<MovieWithGenres> by lazy {
        MutableLiveData<MovieWithGenres>()
    }

    fun getMovieDetails(id: Int) {
        viewModelScope.launch {
            currentMovie.postValue(movieDao.getMovieWithGenres(id))
        }
    }

    @ExperimentalPagingApi
    var mostPopularMovies: LiveData<PagingData<Movie>> = Pager(
        PagingConfig( 20),

        null,  // initialKey
        moviesMediator,
        { movieDao.getMostPopularMovies() }
    ).liveData.cachedIn(this)
}