package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arya.storyapp.local.DataStoreManager
import com.arya.storyapp.model.Story
import com.arya.storyapp.repository.StoryRepository
import com.arya.storyapp.ui.adapter.StoryPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val storyRepository: StoryRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _responseLiveData = MutableLiveData<PagingData<Story>>()
    val responseLiveData: LiveData<PagingData<Story>> get() = _responseLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> get() = _isLoadingLiveData

    private var currentLocation: Int = 0

    fun getAllStories(location: Int) {
        viewModelScope.launch {
            try {
                currentLocation = location
                val factory = { StoryPagingSource(storyRepository, location) }
                val config = PagingConfig(pageSize = 20, enablePlaceholders = false)
                val storiesFlow = Pager(config = config, pagingSourceFactory = factory).flow
                _isLoadingLiveData.value = true
                storiesFlow.cachedIn(viewModelScope).collectLatest { pagingData ->
                    _responseLiveData.value = pagingData
                    _isLoadingLiveData.value = false
                }
            } catch (e: Exception) {
                _isLoadingLiveData.value = false
                _errorLiveData.value = "Failed to fetch stories"
            }
        }
    }
}

