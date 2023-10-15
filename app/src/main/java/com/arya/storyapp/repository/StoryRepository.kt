package com.arya.storyapp.repository

import com.arya.storyapp.api.StoryResponse
import com.arya.storyapp.api.StoryService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class StoryRepository(private val storyService: StoryService) {

    fun getAllStories(token: String, page: Int?, size: Int?, location: Int): Call<StoryResponse> {
        return storyService.getAllStories(page, size, location, "Bearer $token")
    }

    fun getStoryDetails(token: String, storyId: String): Call<StoryResponse> {
        return storyService.getStoryDetails(storyId, "Bearer $token")
    }

    fun addNewStory(
        token: String,
        description: String,
        photoPath: String,
        lat: Float?,
        lon: Float?
    ): Call<StoryResponse> {
        val descriptionPart = MultipartBody.Part.createFormData("description", description)

        val photoFile = File(photoPath)
        val photoRequestBody = RequestBody.create(MediaType.parse("image/*"), photoFile)
        val photoPart = MultipartBody.Part.createFormData("photo", photoFile.name, photoRequestBody)

        val latPart = lat?.let { MultipartBody.Part.createFormData("lat", it.toString()) }
        val lonPart = lon?.let { MultipartBody.Part.createFormData("lon", it.toString()) }

        return storyService.addNewStory(
            "Bearer $token",
            descriptionPart,
            photoPart,
            latPart,
            lonPart
        )
    }
}
