package com.arya.storyapp.api

data class UserRegistrationRequest(
    val name: String,
    val email: String,
    val password: String
)