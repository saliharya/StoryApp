package com.arya.storyapp.di

import com.arya.storyapp.BuildConfig
import com.arya.storyapp.remote.service.AuthService
import com.arya.storyapp.remote.service.StoryService
import com.arya.storyapp.remote.util.Constants
import com.arya.storyapp.remote.util.HttpInterceptor
import com.arya.storyapp.repository.AuthRepository
import com.arya.storyapp.repository.StoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Singleton
    @Provides
    fun provideClient(headerInterceptor: HttpInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(Constants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(Constants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Constants.NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
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

    @Provides
    fun provideAuthRepository(authService: AuthService): AuthRepository {
        return AuthRepository(authService)
    }
}