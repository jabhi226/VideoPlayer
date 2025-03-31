package com.example.videoplayer.features.reel.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.videoplayer.domain.entities.Video
import com.example.videoplayer.domain.repository.VideoListRepository
import java.lang.Exception

class VideoListPagingSource(
    private val repository: VideoListRepository,
) : PagingSource<Int, Video>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 0
    }

    override fun getRefreshKey(state: PagingState<Int, Video>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val videos = getVideos()
            LoadResult.Page(
                data = videos,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (videos.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    private fun getVideos(): List<Video> {
        return repository.getVideos()
    }
}