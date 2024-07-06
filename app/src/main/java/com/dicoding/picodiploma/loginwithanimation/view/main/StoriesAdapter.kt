package com.dicoding.picodiploma.loginwithanimation.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.picodiploma.loginwithanimation.data.remote.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ListStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.story.StoryActivity

class StoriesAdapter :
    PagingDataAdapter<ListStoryItem, StoriesAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: ListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoryItem) {
            binding.tvItemName.text = item.name
            binding.tvItemDesc.text = item.description
            Glide.with(this@ViewHolder.itemView.context).load("${item.photoUrl}")
                .diskCacheStrategy(DiskCacheStrategy.DATA).into(binding.ivItemPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent((it.context), StoryActivity::class.java)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    androidx.core.util.Pair(
                        holder.itemView.findViewById(com.dicoding.picodiploma.loginwithanimation.R.id.iv_item_photo),
                        "sharedImage"
                    ),
                    androidx.core.util.Pair(
                        holder.itemView.findViewById(com.dicoding.picodiploma.loginwithanimation.R.id.tv_item_name),
                        "sharedTitle"
                    ),
                    androidx.core.util.Pair(
                        holder.itemView.findViewById(com.dicoding.picodiploma.loginwithanimation.R.id.tv_item_desc),
                        "sharedDesc"
                    ),
                )
                intent.putExtra("id", "${item.id}")
                it.context.startActivity(intent, options.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
