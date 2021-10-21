package co.feliperivera.mooveitworkshop.views

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.get
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.feliperivera.mooveitworkshop.data.Movie
import co.feliperivera.mooveitworkshop.databinding.ListLayoutBinding
import com.squareup.picasso.Picasso

class MovieRecyclerViewAdapter(
    diffCallback: DiffUtil.ItemCallback<Movie>
) : PagingDataAdapter<Movie, MovieRecyclerViewAdapter.ViewHolder>(diffCallback){

    private val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Movie? = getItem(position)

        if (item != null) {
            val movieImagePath = IMAGE_BASE_URL + item.poster_path
            holder.binding.movieTitle.text = item.title
            holder.binding.movieYear.text = item.release_date?.take(4) ?: ""
            holder.binding.rating.text = item.vote_average.toString()
            Picasso.get()
                .load(movieImagePath)
                .fit()
                .centerInside()
                .into(holder.binding.imageView)
        }
    }

    inner class ViewHolder(val binding: ListLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}

object MovieComparator : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}