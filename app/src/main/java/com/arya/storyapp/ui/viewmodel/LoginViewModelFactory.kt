package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arya.storyapp.api.AuthService
import com.arya.storyapp.util.SessionManager

class LoginViewModelFactory(
    private val authService: AuthService,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authService, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
