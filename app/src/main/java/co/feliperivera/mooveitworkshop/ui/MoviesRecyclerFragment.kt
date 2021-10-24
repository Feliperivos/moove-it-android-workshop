package co.feliperivera.mooveitworkshop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.paging.ExperimentalPagingApi
import co.feliperivera.mooveitworkshop.R
import co.feliperivera.mooveitworkshop.databinding.FragmentRecyclerBinding
import co.feliperivera.mooveitworkshop.ui.viewmodels.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesRecyclerFragment : Fragment() {

    private val localModel : MovieViewModel by activityViewModels()
    private var _binding: FragmentRecyclerBinding? = null
    private val binding get() = _binding!!

    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentRecyclerBinding.inflate(inflater)
        _binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        postponeEnterTransition()
        with(binding.list) {
            val pagingAdapter = MovieRecyclerViewAdapter(MovieComparator) { id, viewImage ->
                openMovieDetailsFragment(id, viewImage)
            }
            adapter = pagingAdapter
            localModel.mostPopularMovies.observe(viewLifecycleOwner) { pagingData ->
                pagingAdapter.submitData(
                    lifecycle,
                    pagingData
                )
                (view?.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openMovieDetailsFragment(id: Int, view: View ){
        val fragment = MovieDetailsFragment()
        localModel.movieId.postValue(id)
        parentFragmentManager.commit {
            //setCustomAnimations(...)
            setReorderingAllowed(true)
            addSharedElement(view, getString(R.string.movie_poster_details))
            replace(R.id.fragment_container_view, fragment)
            addToBackStack(null)
        }

    }

}