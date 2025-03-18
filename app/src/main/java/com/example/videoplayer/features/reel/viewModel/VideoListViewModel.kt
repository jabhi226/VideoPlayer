package com.example.videoplayer.features.reel.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.videoplayer.data.local.entities.Video
import com.example.videoplayer.domain.repository.VideoListRepository
import com.example.videoplayer.features.reel.pagingSource.VideoListPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@Suppress("OPT_IN_USAGE")
@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val repository: VideoListRepository
) : ViewModel() {

    var getVideoListPaging: Flow<PagingData<Video>>
    private val queryStateFlow = MutableStateFlow(0)

    init {
        getVideoListPaging = queryStateFlow.flatMapLatest {
            updateVideoListPaging()
        }
            .shareIn(viewModelScope, SharingStarted.Lazily)

    }

    private fun updateVideoListPaging(): Flow<PagingData<Video>> {
        return Pager(
            config = PagingConfig(pageSize = 3, enablePlaceholders = false),
            pagingSourceFactory = {
                VideoListPagingSource(
                    repository
                )
            }
        ).flow
    }
}