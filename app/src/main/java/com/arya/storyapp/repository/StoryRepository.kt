package com.arya.storyapp.repository

import com.arya.storyapp.remote.response.StoryResponse
import com.arya.storyapp.remote.service.StoryService
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import java.io.File
import javax.inject.Inject

@ViewModelScoped
class StoryRepository @Inject constructor(private val storyService: StoryService) {

    fun getAllStories(token: String, page: Int?, size: Int?, location: Int): Call<StoryResponse> {
        return storyService.getAllStories(
            page, size, location, "Bearer $token"
        )
    }

    fun getStoriesWithLocation(location: Int, token: String): Call<StoryResponse> {
        return storyService.getStoriesWithLocation(location, "Bearer $token")
    }

    fun addStory(
        token: String,
        description: String,
        photoFile: File,
        lat: Float?,
        lon: Float?
    ): Call<StoryResponse> {
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData(
            "photo", photoFile.name, photoFile.asRequestBody("image/*".toMediaTypeOrNull())
        )
        val latPart = lat?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        val lonPart = lon?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

        return storyService.addStory(descriptionPart, photoPart, latPart, lonPart, "Bearer $token")
    }
}
