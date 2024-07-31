package com.dicoding.picodiploma.loginwithanimation.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.remote.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    fun getStories() {
        viewModelScope.launch {
            try {
                val getStory = repository.getStoriesWithLocation()
                Log.d("MapsViewModel", "Success: ${getStory.message}")
                _stories.value = getStory.listStory as List<ListStoryItem>
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
                Log.d("MapsViewModel", "Error: ${errorResponse.message}")
            }
        }
    }
}