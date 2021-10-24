package co.feliperivera.mooveitworkshop.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.feliperivera.mooveitworkshop.data.entities.Review
import co.feliperivera.mooveitworkshop.databinding.ListReviewItemBinding
import com.squareup.picasso.Picasso

class ReviewRecyclerViewAdapter(
    diffCallback: DiffUtil.ItemCallback<Review>
) : PagingDataAdapter<Review, ReviewRecyclerViewAdapter.ViewHolder>(diffCallback){

    private val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w64_and_h64_face"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListReviewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Review? = getItem(position)

        if (item != null) {
            val avatarImagePath = IMAGE_BASE_URL + item.author_avatar
            holder.binding.reviewAuthor.text = item.author
            holder.binding.reviewText.text = item.content
            Picasso.get()
                .load(avatarImagePath)
                .fit()
                .centerInside()
                .into(holder.binding.profileImage)
        }
    }

    inner class ViewHolder(val binding: ListReviewItemBinding) : RecyclerView.ViewHolder(binding.root)
}

object ReviewComparator : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }
}