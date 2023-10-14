package com.arya.storyapp.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserRegistrationRequest(
    val name: String,
    val email: String,
    val password: String
) : Parcelable