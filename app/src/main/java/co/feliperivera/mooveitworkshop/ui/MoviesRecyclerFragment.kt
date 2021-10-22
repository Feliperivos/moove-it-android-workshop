package co.feliperivera.mooveitworkshop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import co.feliperivera.mooveitworkshop.databinding.FragmentRecyclerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesRecyclerFragment : Fragment() {

    private val localModel : MovieViewModel by viewModels()
    private var _binding: FragmentRecyclerBinding? = null
    private val binding get() = _binding!!

    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentRecyclerBinding.inflate(inflater)
        _binding = FragmentRecyclerBinding.inflate(inflater, container, false)

        with(binding.list) {
            val pagingAdapter = MovieRecyclerViewAdapter(MovieComparator)
            adapter = pagingAdapter
            localModel.mostPopularMovies.observe(viewLifecycleOwner) { pagingData ->
                pagingAdapter.submitData(
                    lifecycle,
                    pagingData
                )
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}