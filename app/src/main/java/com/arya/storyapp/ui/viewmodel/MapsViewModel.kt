package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.storyapp.model.Story
import com.arya.storyapp.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
) : ViewModel() {
    private val storiesLiveData: MutableLiveData<List<Story>> = MutableLiveData()
    fun fetchStoriesWithLocation() {
        viewModelScope.launch {
            try {
                val response =
                    storyRepository.getStoriesWithLocation(1).awaitResponse()
                if (response.isSuccessful) {
                    val stories = response.body()?.listStory ?: emptyList()
                    storiesLiveData.value = stories
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getStoriesLiveData(): LiveData<List<Story>> {
        return storiesLiveData
    }
}
