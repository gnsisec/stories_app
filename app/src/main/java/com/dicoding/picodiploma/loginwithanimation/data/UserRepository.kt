package com.dicoding.picodiploma.loginwithanimation.data

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.remote.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.remote.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(name: String, email: String, password: String ) {
        try {
            val register = apiService.register(name, email, password)
            Log.d("register", "Success: ${register.message}")
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            Log.d("register", "Error: ${errorResponse.message}")
            // todo: show error message
        }
    }

    suspend fun login(email: String, password: String) {
        try {
            val login = apiService.login(email, password)
            Log.d("login", "Error: ${login.message}")
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            Log.d("login", "Error: ${errorResponse.message}")
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}