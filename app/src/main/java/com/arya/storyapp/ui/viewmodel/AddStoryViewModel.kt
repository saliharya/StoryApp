package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.storyapp.local.DataStoreManager
import com.arya.storyapp.remote.response.StoryResponse
import com.arya.storyapp.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.await
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _responseLiveData = MutableLiveData<StoryResponse>()

    private val _successLiveData = MutableLiveData<Boolean>()
    val successLiveData: LiveData<Boolean> get() = _successLiveData

    fun addStory(description: String, photoFile: File, lat: Float?, lon: Float?) {
        viewModelScope.launch {
            val token = dataStoreManager.getToken()

            if (token != null) {
                try {
                    val response =
                        storyRepository.addStory(token, description, photoFile, lat, lon).await()
                    _responseLiveData.value = response
                    _successLiveData.value = true
                } catch (_: Exception) {
                }
            }
        }
    }
}
