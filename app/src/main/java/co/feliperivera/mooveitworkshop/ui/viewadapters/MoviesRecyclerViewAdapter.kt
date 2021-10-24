package co.feliperivera.mooveitworkshop.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.feliperivera.mooveitworkshop.data.entities.Movie
import co.feliperivera.mooveitworkshop.databinding.ListLayoutBinding
import com.squareup.picasso.Picasso

class MovieRecyclerViewAdapter(
    diffCallback: DiffUtil.ItemCallback<Movie>,
    private val onItemClicked: (id: Int, view: View) -> Unit
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
            ViewCompat.setTransitionName(holder.binding.cardView, item.id.toString())
            val movieImagePath = IMAGE_BASE_URL + item.poster_path
            holder.binding.movieTitle.text = item.title
            holder.binding.movieYear.text = item.release_date?.take(4) ?: ""
            holder.binding.rating.text = item.vote_average.toString()
            Picasso.get()
                .load(movieImagePath)
                .fit()
                .centerInside()
                .into(holder.binding.imageView)
            holder.binding.cardView.setOnClickListener {
                onItemClicked(item.id, holder.binding.cardView)
            }
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