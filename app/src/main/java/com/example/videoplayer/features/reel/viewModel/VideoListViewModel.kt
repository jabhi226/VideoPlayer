package com.example.videoplayer.features.reel.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.videoplayer.domain.entities.Video
import com.example.videoplayer.domain.repository.VideoListRepository
import com.example.videoplayer.features.reel.pagingSource.VideoListPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val repository: VideoListRepository
) : ViewModel() {

    var getVideoListPaging: MutableStateFlow<PagingData<Video>> =
        MutableStateFlow(value = PagingData.empty())

    init {
        viewModelScope.launch {
            getVideos()
        }
    }

    private suspend fun getVideos() {
        updateVideoListPaging()
            .distinctUntilChanged()
            .collect {
                getVideoListPaging.value = it
            }
    }

    private fun updateVideoListPaging(): Flow<PagingData<Video>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1, enablePlaceholders = false
            ),
            pagingSourceFactory = {
                VideoListPagingSource(
                    repository
                )
            }
        ).flow.cachedIn(viewModelScope)
    }
}