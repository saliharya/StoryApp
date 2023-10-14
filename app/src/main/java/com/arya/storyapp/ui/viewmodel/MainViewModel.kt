package com.arya.storyapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arya.storyapp.model.Story
import com.arya.storyapp.repository.StoryRepository
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

                    response.body()?.let { storyResponse ->
                        if (response.isSuccessful) {
                            _responseLiveData.value = storyResponse.listStory
                        } else {
                            Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                            _errorLiveData.value = "Failed to fetch stories"
                        }
                    } ?: run { _errorLiveData.value = "Failed to fetch stories" }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    _isLoadingLiveData.value = false
                    _errorLiveData.value = "Network error occurred"
                }
            })
    }
}