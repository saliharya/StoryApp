package com.arya.storyapp.api

data class UserLoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult?
)