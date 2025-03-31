package com.example.videoplayer.domain.repository

import com.example.videoplayer.domain.entities.Video

interface VideoListRepository {

    fun getVideos(): List<Video>

}