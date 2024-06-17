package com.dicoding.picodiploma.loginwithanimation.view.story

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.story.observe(this) {
            binding.tvDetailName.text = it.name.toString()
            binding.tvDetailDesc.text = it.description.toString()
            Glide.with(this).load("${it.photoUrl}").diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.ivDetailPhoto)
        }

        val id = intent?.getStringExtra("id").toString()
        viewModel.getDetailStory(id)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}