package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arya.storyapp.api.AuthService

class RegisterViewModelFactory(private val authService: AuthService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
