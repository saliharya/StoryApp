package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arya.storyapp.remote.service.AuthService
import com.arya.storyapp.remote.request.UserRegistrationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authService: AuthService) : ViewModel() {
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

