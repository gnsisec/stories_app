package com.dicoding.picodiploma.loginwithanimation.view.story

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.story.observe(this) {
            binding.tvDetailName.text = it.name.toString()
            binding.tvDetailDesc.text = it.description.toString()
            Glide.with(this).load("${it.photoUrl}")
                .diskCacheStrategy(DiskCacheStrategy.DATA).into(binding.ivDetailPhoto)
        }

        val id = intent?.getStringExtra("id").toString()
        viewModel.getDetailStory(id)
    }
}