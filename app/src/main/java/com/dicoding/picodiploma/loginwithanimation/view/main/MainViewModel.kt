package com.dicoding.picodiploma.loginwithanimation.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val getStory = repository.getStories()
                Log.d("main", "Success: ${getStory.message}")
                _stories.value = getStory.listStory as List<ListStoryItem>
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
                Log.d("main", "Error: ${errorResponse.message}")
            }
            _isLoading.value = false
        }
    }
}