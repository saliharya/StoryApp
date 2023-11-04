package com.arya.storyapp.remote.response

data class UserLoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult?
)