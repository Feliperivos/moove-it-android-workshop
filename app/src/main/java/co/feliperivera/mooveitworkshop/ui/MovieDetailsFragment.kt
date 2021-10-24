package co.feliperivera.mooveitworkshop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.transition.TransitionInflater
import co.feliperivera.mooveitworkshop.R
import co.feliperivera.mooveitworkshop.databinding.FragmentMovieDetailBinding
import co.feliperivera.mooveitworkshop.ui.viewmodels.MovieViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment: Fragment() {

    private val activityModel : MovieViewModel by activityViewModels()
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185"
    private val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w533_and_h300_bestv2"
    private var youtubeVideo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.basic_transition)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentMovieDetailBinding.inflate(inflater)
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.reviewsButton.setOnClickListener {
            openMovieReviewsFragment()
        }

        activityModel.movieId.observe(viewLifecycleOwner) {  id ->
            activityModel.getMovieDetails(id)
        }
        activityModel.currentMovie.observe(viewLifecycleOwner) {  movie ->
            binding.movieTitle.text = movie.data.title
            binding.ratingBar.rating = (movie.data.vote_average) / 2
            binding.ratingValue.text = movie.data.vote_average.toString()
            binding.overview.text = movie.data.overview
            if(movie.genres.isNotEmpty()){
                var genresString = ""
                for(i in movie.genres.indices){
                    genresString += if(i == 0){
                        movie.genres[i].name
                    }else{
                        ", " + movie.genres[i].name
                    }
                }
                binding.genres.text = genresString
            }
            if(movie.data.poster_path != null){
                val movieImagePath = POSTER_BASE_URL + movie.data.poster_path
                Picasso.get()
                    .load(movieImagePath)
                    .fit()
                    .centerInside()
                    .into(binding.poster)
            }


            youtubeVideo = if(movie.data.video_key != null){
                movie.data.video_key!!
            }else {
                ""
            }

            if(youtubeVideo.isNotEmpty()){
                binding.backdrop.visibility = View.INVISIBLE
                binding.youtubePlayerView.visibility = View.VISIBLE
                lifecycle.addObserver(binding.youtubePlayerView)
                binding.youtubePlayerView.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                        val videoId = youtubeVideo
                        youTubePlayer.cueVideo(videoId, 0f)
                    }
                })
            }else{
                if(movie.data.backdrop_path != null){
                    binding.youtubePlayerView.visibility = View.INVISIBLE
                    val movieImagePath = BACKDROP_BASE_URL + movie.data.backdrop_path
                    Picasso.get()
                        .load(movieImagePath)
                        .fit()
                        .centerCrop()
                        .into(binding.backdrop)
                    binding.backdrop.visibility = View.VISIBLE
                }else{
                    binding.backdrop.visibility = View.INVISIBLE
                }

            }
        }

        return binding.root
    }

    private fun openMovieReviewsFragment() {
        val fragment = ReviewsFragment()
        parentFragmentManager.commit {
            //setCustomAnimations(...)
            setReorderingAllowed(true)
            addSharedElement(binding.poster, getString(R.string.movie_poster_review))
            replace(R.id.fragment_container_view, fragment)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}