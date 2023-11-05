package com.example.videoplayer.modules.repository

import android.content.Context
import android.net.Uri
import com.example.videoplayer.modules.models.Video
import java.io.File
import java.util.Date

class VideoListRepositoryImpl(val context: Context) : VideoListRepository {
    override fun getVideos(): List<Video> {
        val list = arrayListOf<Video>()
        for (i in 1 .. 3) {
            val videoUri = Uri.fromFile(File("//android_asset/video${i}.mp4"))
            list.add(Video(Date().time - (i * 1000), videoUri))
        }
        return list
    }
}