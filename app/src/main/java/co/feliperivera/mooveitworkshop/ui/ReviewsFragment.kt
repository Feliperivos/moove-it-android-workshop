package co.feliperivera.mooveitworkshop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import co.feliperivera.mooveitworkshop.R
import co.feliperivera.mooveitworkshop.databinding.FragmentRecyclerBinding
import co.feliperivera.mooveitworkshop.databinding.FragmentReviewsBinding
import co.feliperivera.mooveitworkshop.ui.viewmodels.MovieViewModel
import co.feliperivera.mooveitworkshop.ui.viewmodels.ReviewViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewsFragment: Fragment() {

    private val activityModel : MovieViewModel by activityViewModels()
    private val reviewsViewModel : ReviewViewModel by viewModels()
    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!
    private val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.basic_transition)
         activityModel.movieId.value?.let { reviewsViewModel.movieId = it }
    }

    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentRecyclerBinding.inflate(inflater)
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        reviewsViewModel.numberReviews.observe(viewLifecycleOwner) {  numberReviews ->
            binding.reviewTitle.text = getString(R.string.reviews) + " (" + numberReviews + ")"
        }


        activityModel.currentMovie.observe(viewLifecycleOwner) {  movie ->
            if(movie.data.poster_path != null){
                val movieImagePath = POSTER_BASE_URL + movie.data.poster_path
                Picasso.get()
                    .load(movieImagePath)
                    .fit()
                    .centerInside()
                    .into(binding.poster , object: com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        startPostponedEnterTransition()
                    }

                    override fun onError(e: java.lang.Exception?) {
                    }
                })
            }

        }
        with(binding.listReviews) {
            layoutManager = LinearLayoutManager(context)
            val pagingAdapter = ReviewRecyclerViewAdapter(ReviewComparator)
            adapter = pagingAdapter
            reviewsViewModel.movieReviews.observe(viewLifecycleOwner) { pagingData ->
                activityModel.movieId.value?.let { reviewsViewModel.updateNumberReviews(it) }
                pagingAdapter.submitData(
                    lifecycle,
                    pagingData
                )
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}