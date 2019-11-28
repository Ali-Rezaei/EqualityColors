package com.sample.android.storytel.ui

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.android.storytel.databinding.PostItemBinding
import com.sample.android.storytel.domain.Photo
import com.sample.android.storytel.domain.Post
import com.sample.android.storytel.util.layoutInflater

class MainAdapter(
    private val callback: OnClickListener
) : ListAdapter<Post, MainAdapter.MainViewHolder>(DiffCallback) {

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MainViewHolder.from(parent)

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position), callback)
    }

    /**
     * ViewHolder for Post items. All work is done by data binding.
     */
    class MainViewHolder(private val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Post, postCallback: OnClickListener) {
            with(binding) {
                post = item
                poster = image
                callback = postCallback
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): MainViewHolder {
                val binding = PostItemBinding.inflate(
                    parent.context.layoutInflater,
                    parent,
                    false
                )
                return MainViewHolder(binding)
            }
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Post]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Post] & [Photo]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Post] & [Photo]
     */
    class OnClickListener(val clickListener: (post: Post, imageView: ImageView) -> Unit) {
        fun onClick(post: Post, imageView: ImageView) =
            clickListener(post, imageView)
    }
}