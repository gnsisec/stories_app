package com.dicoding.picodiploma.loginwithanimation.view.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.remote.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.Story
import com.dicoding.picodiploma.loginwithanimation.data.remote.StoryDetailResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


class StoryViewModel (private val repository: UserRepository) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _story = MutableLiveData<Story>()
    val story: LiveData<Story> = _story

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            try {
                val getStoryDetail = repository.getStoryDetail(id)
                Log.d("main", "Success: ${getStoryDetail.message}")
                _story.value = getStoryDetail.story as Story
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, StoryDetailResponse::class.java)
                Log.d("main", "Error: ${errorResponse.message}")
            }
        }
    }
}
