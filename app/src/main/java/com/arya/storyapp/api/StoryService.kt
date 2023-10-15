package com.arya.storyapp.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryService {
    @GET("stories")
    fun getAllStories(
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int,
        @Header("Authorization") token: String
    ): Call<StoryResponse>

    @GET("stories/{id}")
    fun getStoryDetails(
        @Path("id") storyId: String,
        @Header("Authorization") token: String
    ): Call<StoryResponse> // You can adjust the response type accordingly

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") token: String,
        @Part description: MultipartBody.Part,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: MultipartBody.Part?,
        @Part("lon") lon: MultipartBody.Part?
    ): Call<StoryResponse>
}