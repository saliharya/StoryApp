package com.arya.storyapp.di

import com.arya.storyapp.api.AuthService
import com.arya.storyapp.api.StoryService
import com.arya.storyapp.repository.StoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideStoryService(retrofit: Retrofit): StoryService {
        return retrofit.create(StoryService::class.java)
    }

    @Provides
    @Singleton
    fun provideStoryRepository(storyService: StoryService): StoryRepository {
        return StoryRepository(storyService)
    }
}