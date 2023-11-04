package com.arya.storyapp.remote.service

import com.arya.storyapp.remote.request.UserLoginRequest
import com.arya.storyapp.remote.response.UserLoginResponse
import com.arya.storyapp.remote.request.UserRegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("register")
    suspend fun registerUser(@Body request: UserRegistrationRequest): Response<UserLoginResponse>

    @POST("login")
    suspend fun loginUser(@Body request: UserLoginRequest): Response<UserLoginResponse>
}