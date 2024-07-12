package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.LoadingStateAdapter
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.maps.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.view.upload.UploadActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import okhttp3.internal.notifyAll

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvStories.layoutManager = LinearLayoutManager(this)

        val toolbar: androidx.appcompat.widget.Toolbar = binding.myToolbar
        setSupportActionBar(toolbar)

        val adapter = StoriesAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            binding.rvStories.visibility = View.VISIBLE
        }

        viewModel.stories.observe(this) {
            adapter.submitData(lifecycle, it)
            binding.rvStories.scrollToPosition(0)
        }

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logout()
                true
            }

            R.id.ic_map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> return true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}