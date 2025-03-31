package com.example.videoplayer.data.repository

import android.net.Uri
import com.example.videoplayer.domain.entities.Video
import com.example.videoplayer.domain.repository.VideoListRepository
import java.io.File
import java.util.Date

class VideoListLocalRepositoryImpl : VideoListRepository {
    override fun getVideos(): List<Video> {
        val list = arrayListOf<Video>()
        for (i in 1..3) {
            val videoUri = Uri.fromFile(File("//android_asset/video${i}.mp4"))
            list.add(
                Video(
                    Date().time - (i * 1000),
                    videoUri.toString(),
                    "title",
                    "subtitle",
                    "description",
                    null
                )
            )
        }
        return list
    }
}