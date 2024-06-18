package com.dicoding.picodiploma.loginwithanimation.view.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.remote.UploadResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UploadViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _finishActivityEvent = MutableSharedFlow<Unit>()
    val finishActivityEvent = _finishActivityEvent.asSharedFlow()

    private val _uploadState = MutableLiveData<UploadResponse>()
    val uploadState: LiveData<UploadResponse> = _uploadState

    private fun triggerFinishActivity() {
        viewModelScope.launch {
            _finishActivityEvent.emit(Unit)
        }
    }

    fun uploadStory(file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _uploadState.value = repository.uploadStory(file, description)
                Log.d("upload", "Success: ${_uploadState.value!!.message}")
                triggerFinishActivity()
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
                _uploadState.value = errorResponse
                Log.d("upload", "Error: ${errorResponse.message}")
            }
            _isLoading.value = false
        }
    }
}