package com.dicoding.picodiploma.loginwithanimation.data.remote

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): SignUpResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): SignInResponse

    @GET("stories")
    suspend fun getStories(@Query("page") page: Int = 1, @Query("size") size: Int = 15): StoryResponse

   @GET("stories/{id}")
   suspend fun  getDetailStory(@Path("id") id: String): StoryDetailResponse

}