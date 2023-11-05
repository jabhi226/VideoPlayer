package com.example.videoplayer.di

import android.content.Context
import com.example.videoplayer.modules.repository.VideoListRepository
import com.example.videoplayer.modules.repository.VideoListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class VideoPlayerModule {


    @Provides
    @Singleton
    fun provideVideoListRepository(@ApplicationContext context: Context): VideoListRepository {
        return VideoListRepositoryImpl(context)
    }


}