package com.arya.storyapp.remote.request

data class UserRegistrationRequest(
    val name: String,
    val email: String,
    val password: String
)