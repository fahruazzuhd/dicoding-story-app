package com.fahruaz.storyapp.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import com.fahruaz.storyapp.Response.ListStoryItem
import com.fahruaz.storyapp.api.ApiService
import com.fahruaz.storyapp.data.StoryRemoteMediator
import com.fahruaz.storyapp.data.StoryRepository
import com.fahruaz.storyapp.database.StoryDatabase
import com.fahruaz.storyapp.models.UserModel
import com.fahruaz.storyapp.preferences.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreference,
                    storyRepository: StoryRepository,
                    private val storyDB: StoryDatabase,
                    private val apiService: ApiService
) : ViewModel() {

    val listStories: LiveData<PagingData<ListStoryItem>> = storyRepository.getStory().cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    fun getStoryPaging(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDB, apiService),
            pagingSourceFactory = {
                storyDB.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}