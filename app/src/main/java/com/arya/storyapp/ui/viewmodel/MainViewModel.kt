package com.arya.storyapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.storyapp.local.DataStoreManager
import com.arya.storyapp.model.Story
import com.arya.storyapp.remote.response.StoryResponse
import com.arya.storyapp.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _responseLiveData = MutableLiveData<List<Story>>()
    val responseLiveData: LiveData<List<Story>> get() = _responseLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> get() = _isLoadingLiveData

    fun getAllStories(bearerToken: String) {
        _isLoadingLiveData.value = true

        viewModelScope.launch {
            dataStoreManager.tokenFlow.collect { token ->
                if (token != null) {
                    loadStories(bearerToken)
                } else {
                    _isLoadingLiveData.value = false
                }
            }
        }
    }

    private fun loadStories(bearerToken: String) {
        storyRepository.getAllStories(bearerToken, null, null, 0)
            .enqueue(object : Callback<StoryResponse> {
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    _isLoadingLiveData.value = false

                    if (response.isSuccessful) {
                        response.body()?.listStory?.let { storyList ->
                            _responseLiveData.value = storyList
                        } ?: run { handleApiError("Failed to fetch stories") }
                    } else {
                        handleApiError(response.errorBody()?.string())
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    _isLoadingLiveData.value = false
                    _errorLiveData.value = "Network error occurred"
                }
            })
    }

    private fun handleApiError(errorString: String?) {
        Log.e("API_ERROR", "Error: $errorString")
        _errorLiveData.value = "Failed to fetch stories"
    }
}
