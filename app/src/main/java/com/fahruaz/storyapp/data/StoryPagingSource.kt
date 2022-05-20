package com.fahruaz.storyapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fahruaz.storyapp.Response.ListStoryItem
import com.fahruaz.storyapp.api.ApiService
import com.fahruaz.storyapp.ui.LoginActivity
import com.fahruaz.storyapp.ui.MainActivity

class StoryPagingSource(private val apiService: ApiService): PagingSource<Int, ListStoryItem>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX

            val responseData = apiService.getStoryPaging(
                "Bearer ${LoginActivity.token}",
                position,
                params.loadSize
            )

            MainActivity.listStories.addAll(responseData.listStory as List<ListStoryItem>)
            LoadResult.Page(
                data = MainActivity.listStories,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (MainActivity.listStories.isNullOrEmpty()) null else position + 1
            )

        } catch (exception: Exception) {
            Log.e("catch", MainActivity.listStories.toString())
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }


}
