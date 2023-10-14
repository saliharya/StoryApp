package com.arya.storyapp.api

import retrofit2.Call

class StoryRepository(private val storyService: StoryService) {

    fun getAllStories(token: String, page: Int?, size: Int?, location: Int): Call<StoryResponse> {
        return storyService.getAllStories(page, size, location, token)
    }
}