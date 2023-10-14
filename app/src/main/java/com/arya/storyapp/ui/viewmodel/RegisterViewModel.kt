package com.arya.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.arya.storyapp.api.AuthService
import com.arya.storyapp.api.UserLoginResponse
import com.arya.storyapp.api.UserRegistrationRequest
import retrofit2.Call

class RegisterViewModel(private val authService: AuthService) : ViewModel() {
    fun registerUser(name: String, email: String, password: String): Call<UserLoginResponse> {
        val request = UserRegistrationRequest(name, email, password)
        return authService.registerUser(request)
    }
}
