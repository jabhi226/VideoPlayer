package com.example.videoplayer.data.entities

import com.google.gson.annotations.SerializedName


data class VideoResponse(
    @SerializedName("categories") var categories: ArrayList<Categories> = arrayListOf()
)

data class Categories(
    @SerializedName("name") var name: String? = null,
    @SerializedName("videos") var videos: ArrayList<Videos> = arrayListOf()
)

data class Videos(
    @SerializedName("description") var description: String,
    @SerializedName("sources") var sources: ArrayList<String> = arrayListOf(),
    @SerializedName("subtitle") var subtitle: String,
    @SerializedName("thumb") var thumb: String? = null,
    @SerializedName("title") var title: String

)

