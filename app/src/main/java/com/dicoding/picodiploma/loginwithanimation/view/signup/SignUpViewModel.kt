package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.remote.SignUpResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _signup = MutableLiveData<SignUpResponse>()
    val signup: LiveData<SignUpResponse> = _signup

    fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _signup.value = repository.signup(name, email, password)
                Log.d("register", "Success: ${_signup.value!!.message}")
                Log.d("register", "Success: ${_signup.value!!.error}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, SignUpResponse::class.java)
                _signup.value = errorResponse
                Log.d("register", "Error: ${errorResponse.message}")
                Log.d("register", "Error: ${errorResponse.error}")
            }
            _isLoading.value = false
        }
    }
}
