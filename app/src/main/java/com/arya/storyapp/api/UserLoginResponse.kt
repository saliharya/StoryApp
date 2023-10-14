package com.arya.storyapp.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserLoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult?
) : Parcelable