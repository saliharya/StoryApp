package com.arya.storyapp.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
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
}