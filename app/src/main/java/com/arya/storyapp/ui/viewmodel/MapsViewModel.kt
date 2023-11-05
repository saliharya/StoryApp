package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.arya.storyapp.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {

    fun getAllStories(token: String, location: Int) =
        storyRepository.getAllStories(token, null, null, location)
}