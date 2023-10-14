package com.arya.storyapp.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("register")
    fun registerUser(@Body request: UserRegistrationRequest): Call<UserLoginResponse>

    @POST("login")
    fun loginUser(@Body request: UserLoginRequest): Call<UserLoginResponse>
}