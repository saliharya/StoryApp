package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arya.storyapp.api.AuthService
import com.arya.storyapp.util.DataStoreManager

class LoginViewModelFactory(
    private val authService: AuthService,
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AuthService::class.java, DataStoreManager::class.java)
            .newInstance(authService, dataStoreManager)
    }
}
