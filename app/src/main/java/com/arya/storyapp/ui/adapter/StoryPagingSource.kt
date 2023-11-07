package com.arya.storyapp.ui.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arya.storyapp.model.Story
import com.arya.storyapp.remote.service.StoryService
import retrofit2.HttpException
import retrofit2.awaitResponse
import java.io.IOException

class StoryPagingSource(
    private val storyService: StoryService,
) : PagingSource<Int, Story>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        val page = params.key ?: 1
        return try {
            val response = storyService.getAllStories(page, 10, 0).awaitResponse()
            val stories = response.body()?.listStory ?: emptyList()

            LoadResult.Page(
                data = stories,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (stories.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition
    }
}