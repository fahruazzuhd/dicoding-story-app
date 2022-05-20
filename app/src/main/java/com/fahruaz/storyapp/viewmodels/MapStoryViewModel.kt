package com.fahruaz.storyapp.viewmodels

import androidx.lifecycle.*
import com.fahruaz.storyapp.Response.GetStoryResponse
import com.fahruaz.storyapp.Response.ListStoryItem
import com.fahruaz.storyapp.api.ApiConfig
import com.fahruaz.storyapp.models.UserModel
import com.fahruaz.storyapp.preferences.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapStoryViewModel(private val pref: UserPreference): ViewModel() {
    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStory


    fun getAllStory(token: String) {
        val service = ApiConfig().getApiService().getAllStories(token, (1).toString())

        service.enqueue(object : Callback<GetStoryResponse> {
            override fun onResponse(call: Call<GetStoryResponse>, response: Response<GetStoryResponse>) {

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error!!) {
                        _listStory.value = (responseBody.listStory as List<ListStoryItem>?)!!
                    }
                }
            }
            override fun onFailure(call: Call<GetStoryResponse>, t: Throwable) {
               //DO NOTHING
            }
        })
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