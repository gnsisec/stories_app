package com.dicoding.picodiploma.loginwithanimation.view.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.picodiploma.loginwithanimation.data.remote.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ListStoryBinding

class StoriesAdapter :
    ListAdapter<ListStoryItem, StoriesAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: ListStoryBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: ListStoryItem) {
                binding.tvItemName.text = item.name
                binding.tvItemDesc.text = item.description
                Glide.with(this@ViewHolder.itemView.context).load("${item.photoUrl}")
                    .diskCacheStrategy(DiskCacheStrategy.DATA).into(binding.ivItemPhoto)
                Log.d("StoriesAdapter", "${item.photoUrl}")
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
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
