package com.fahruaz.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.fahruaz.storyapp.Response.ListStoryItem
import com.fahruaz.storyapp.api.ApiService
import com.fahruaz.storyapp.database.StoryDatabase

class StoryRepository(private val apiService: ApiService, private val storyDatabase: StoryDatabase) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}