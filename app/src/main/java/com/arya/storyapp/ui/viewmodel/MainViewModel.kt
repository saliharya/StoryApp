package com.arya.storyapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arya.storyapp.api.Story
import com.arya.storyapp.api.StoryRepository
import com.arya.storyapp.api.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _responseLiveData = MutableLiveData<List<Story>>()
    val responseLiveData: LiveData<List<Story>> get() = _responseLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> get() = _isLoadingLiveData

    fun getAllStories(bearerToken: String) {
        _isLoadingLiveData.value = true

        storyRepository.getAllStories("Bearer $bearerToken", null, null, 0)
            .enqueue(object : Callback<StoryResponse> {
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    _isLoadingLiveData.value = false

                    if (response.isSuccessful) {
                        val storyResponse = response.body()
                        val stories = storyResponse?.listStory
                        _responseLiveData.value = stories
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("API_ERROR", "Error: $errorBody")
                        _errorLiveData.value = "Failed to fetch stories"
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    _isLoadingLiveData.value = false
                    _errorLiveData.value = "Network error occurred"
                }
            })
    }
}

