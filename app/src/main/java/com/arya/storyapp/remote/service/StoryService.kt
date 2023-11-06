package com.arya.storyapp.remote.service

import com.arya.storyapp.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface StoryService {
    @GET("stories")
    fun getAllStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int,
        @Header("Authorization") token: String
    ): Call<StoryResponse>

    @GET("stories")
    fun getStoriesWithLocation(
        @Query("location") location: Int = 1,
        @Header("Authorization") token: String,
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
        @Header("Authorization") token: String
    ): Call<StoryResponse>
}