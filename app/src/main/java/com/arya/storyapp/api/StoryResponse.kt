package com.arya.storyapp.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<Story>
) : Parcelable