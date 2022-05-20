package com.fahruaz.storyapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fahruaz.storyapp.Response.ListStoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryItem>)

    @Query("SELECT * FROM stories")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @Query("SELECT * FROM stories")
    fun getAllStoryOffline(): Flow<List<ListStoryItem>>

    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}