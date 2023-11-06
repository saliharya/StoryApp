package com.arya.storyapp.repository

import com.arya.storyapp.remote.request.UserLoginRequest
import com.arya.storyapp.remote.request.UserRegistrationRequest
import com.arya.storyapp.remote.response.UserLoginResponse
import com.arya.storyapp.remote.service.AuthService
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Response
import javax.inject.Inject

@ViewModelScoped
class AuthRepository @Inject constructor(private val authService: AuthService) {

    suspend fun registerUser(request: UserRegistrationRequest): Response<UserLoginResponse> {
        return authService.registerUser(request)
    }

    suspend fun loginUser(request: UserLoginRequest): Response<UserLoginResponse> {
        return authService.loginUser(request)
    }
}