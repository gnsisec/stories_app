package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.remote.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.remote.SignInResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.SignUpResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.StoryDetailResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.UploadResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    suspend fun signup(name: String, email: String, password: String): SignUpResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): SignInResponse {
        return apiService.login(email, password)
    }

    suspend fun getStories(): StoryResponse {
        return apiService.getStories()
    }

    suspend fun getStoryDetail(id: String): StoryDetailResponse {
        return apiService.getDetailStory(id)
    }

    suspend fun uploadStory(file: MultipartBody.Part, description: RequestBody): UploadResponse {
        return apiService.uploadStory(file, description)
    }

    companion object {

        fun getInstance(userPreference: UserPreference, apiService: ApiService): UserRepository =
            UserRepository(userPreference, apiService)
//        @Volatile
//        private var instance: UserRepository? = null
//        fun getInstance(
//            userPreference: UserPreference,
//            apiService: ApiService
//        ): UserRepository =
//            instance ?: synchronized(this) {
//                instance ?: UserRepository(userPreference, apiService)
//            }.also { instance = it }
//    }
    }
}