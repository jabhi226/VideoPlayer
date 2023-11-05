package com.example.videoplayer.modules.repository

import com.example.videoplayer.modules.models.Video

interface VideoListRepository {

    fun getVideos(): List<Video>
}