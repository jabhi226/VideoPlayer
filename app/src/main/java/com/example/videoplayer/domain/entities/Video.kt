package com.example.videoplayer.domain.entities

data class Video(
    val id: Long,
    val urlString: String,
    val title: String,
    val subTitle: String,
    val description: String,
    val thumbnailUrl: String?
)
