package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.storyapp.api.AuthService
import com.arya.storyapp.api.UserRegistrationRequest
import kotlinx.coroutines.launch

class RegisterViewModel(private val authService: AuthService) : ViewModel() {
    val registrationResult = MutableLiveData<Boolean>()

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response =
                    authService.registerUser(UserRegistrationRequest(name, email, password))
                registrationResult.value = response.isSuccessful
            } catch (e: Exception) {
                registrationResult.value = false
            }
        }
    }
}

