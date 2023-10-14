package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arya.storyapp.api.StoryRepository

class MainViewModelFactory(private val storyRepository: StoryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(StoryRepository::class.java).newInstance(storyRepository)
    }
}