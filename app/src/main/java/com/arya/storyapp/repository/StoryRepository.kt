package com.arya.storyapp.repository

import com.arya.storyapp.api.StoryResponse
import com.arya.storyapp.api.StoryService
import retrofit2.Call

class StoryRepository(private val storyService: StoryService) {

    fun getAllStories(token: String, page: Int?, size: Int?, location: Int): Call<StoryResponse> {
        return storyService.getAllStories(page, size, location, "Bearer $token")
    }
}