package com.arya.storyapp.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResult(
    val userId: String,
    val name: String,
    val token: String
) : Parcelable