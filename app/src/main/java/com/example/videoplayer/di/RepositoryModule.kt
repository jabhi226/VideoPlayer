package com.example.videoplayer.di

import android.content.Context
import com.example.videoplayer.domain.repository.VideoListRepository
import com.example.videoplayer.data.repository.VideoListLocalRepositoryImpl
import com.example.videoplayer.data.repository.VideoListRemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {


    @Provides
    @Singleton
    fun provideVideoListRepository(@ApplicationContext context: Context): VideoListRepository {
//        return VideoListLocalRepositoryImpl()
        return VideoListRemoteRepositoryImpl()
    }


}