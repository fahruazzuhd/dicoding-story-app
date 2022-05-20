package com.fahruaz.storyapp.di

import android.content.Context
import com.fahruaz.storyapp.api.ApiConfig
import com.fahruaz.storyapp.api.ApiService
import com.fahruaz.storyapp.data.StoryRepository
import com.fahruaz.storyapp.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig().getApiService()
        return StoryRepository(apiService, database)
    }

    fun provideDatabase(context: Context): StoryDatabase {
        return StoryDatabase.getDatabase(context)
    }

    fun provideApiService(): ApiService{
        return  ApiConfig().getApiService()
    }
}