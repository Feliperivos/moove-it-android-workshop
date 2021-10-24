package co.feliperivera.mooveitworkshop.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import co.feliperivera.mooveitworkshop.data.MyDatabase
import co.feliperivera.mooveitworkshop.data.WebService
import co.feliperivera.mooveitworkshop.data.entities.Review
import co.feliperivera.mooveitworkshop.data.remotemediators.ReviewRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val db: MyDatabase,
    private val webService: WebService
): ViewModel() {

    var movieId: Int = 0

    val numberReviews: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    fun updateNumberReviews(id: Int) {
        viewModelScope.launch {
            numberReviews.postValue(db.movieDao().getMovieWithGenres(id).data.reviews)
        }
    }

    @ExperimentalPagingApi
    val movieReviews: LiveData<PagingData<Review>> by lazy {
        Pager(
            PagingConfig( 20),
            null,  // initialKey
            remoteMediator = ReviewRemoteMediator(movieId, db, webService),
            { db.reviewDao().getReviews() }
        ).liveData
    }
}