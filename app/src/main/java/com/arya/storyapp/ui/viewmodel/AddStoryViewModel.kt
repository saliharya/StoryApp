package com.arya.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.arya.storyapp.api.StoryResponse
import com.arya.storyapp.repository.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    fun addNewStory(
        token: String,
        description: String,
        photoPath: String,
        lat: Float?,
        lon: Float?
    ) {
        repository.addNewStory(token, description, photoPath, lat, lon)
            .enqueue(object : Callback<StoryResponse> {
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    if (response.isSuccessful) {
                    } else {
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                }
            })
    }
}
