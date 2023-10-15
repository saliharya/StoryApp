package com.arya.storyapp.api

data class UserLoginRequest(
    val email: String,
    val password: String
)