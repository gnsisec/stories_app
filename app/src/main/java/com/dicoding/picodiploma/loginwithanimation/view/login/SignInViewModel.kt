package com.dicoding.picodiploma.loginwithanimation.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.SignInResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.SignUpResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignInViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> = _loading

    private val _sign_in = MutableLiveData<SignInResponse>()
    val sign_in : LiveData<SignInResponse> = _sign_in

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _sign_in.value = repository.login(email, password)
                saveSession( UserModel(email, _sign_in.value!!.loginResult.token) )
                Log.d("login", "Success: ${_sign_in.value!!.message}")
                Log.d("login", "Success: ${_sign_in.value!!.error}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, SignInResponse::class.java)
                _sign_in.value = errorResponse
                Log.d("login", "Error: ${errorResponse.message}")
            }
        }
    }
}