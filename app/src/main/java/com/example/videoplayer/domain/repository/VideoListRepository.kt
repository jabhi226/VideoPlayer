package com.example.videoplayer.domain.repository

import com.example.videoplayer.data.local.entities.Video

interface VideoListRepository {

    fun getVideos(): List<Video>
}