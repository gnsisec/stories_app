package com.dicoding.picodiploma.loginwithanimation.view.upload

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.remote.UploadResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UploadViewModel (private val repository: UserRepository) : ViewModel() {
    fun uploadStory(file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            try {
                val upload = repository.uploadStory(file, description)
                Log.d("upload", "Success: ${upload.message}")
            } catch (e:HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
                Log.d("main", "Error: ${errorResponse.message}")
            }
        }
    }
}