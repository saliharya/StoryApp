package com.arya.storyapp.remote.request

data class UserLoginRequest(
    val email: String,
    val password: String
)