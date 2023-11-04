package com.arya.storyapp.remote.response

import android.os.Parcelable
import com.arya.storyapp.model.Story
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<Story>
) : Parcelable