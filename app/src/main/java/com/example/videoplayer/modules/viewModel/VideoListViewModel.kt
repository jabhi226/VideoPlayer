package com.example.videoplayer.modules.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.videoplayer.modules.models.Video
import com.example.videoplayer.modules.repository.VideoListRepository
import com.example.videoplayer.modules.ui.adapter.VideoListPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val repository: VideoListRepository
) : ViewModel() {


    var getVideoListPaging: Flow<PagingData<Video>>
    val queryStateFlow = MutableStateFlow(0)

    init {
        getVideoListPaging = queryStateFlow.flatMapLatest {
            updatevideoListPaging()
        }
            .shareIn(viewModelScope, SharingStarted.Lazily)

    }

    private suspend fun updatevideoListPaging(): Flow<PagingData<Video>> {

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