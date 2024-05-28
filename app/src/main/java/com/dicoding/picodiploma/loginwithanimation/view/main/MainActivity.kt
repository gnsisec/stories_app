package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            viewModel.getStories()
        }

        viewModel.stories.observe(this) { story ->
            val adapter = StoriesAdapter()
            adapter.submitList(viewModel.stories.value)
            binding.rvStories.adapter = adapter
            binding.rvStories.visibility = View.VISIBLE
        }

        setupView()
        setupAction()

        val storyListManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = storyListManager
//        val itemDecoration = DividerItemDecoration(this, storyListManager.orientation)
//        val drawable = GradientDrawable()
//        drawable.setSize(10, 10)
//        drawable.setStroke(2, Color.BLACK)
//        itemDecoration.setDrawable(drawable)
//        binding.rvStories.addItemDecoration(itemDecoration)
    }

//    private fun displayResult() {
//        val adapter = StoriesAdapter()
//        adapter.submitList(searchViewModel.searchResult.value)
//    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
//        binding.actionLogout.setOnClickListener {
//            viewModel.logout()
//        }
    }

}