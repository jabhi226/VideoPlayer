package com.example.videoplayer.modules.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayer.R
import com.example.videoplayer.databinding.ItemVideoBinding
import com.example.videoplayer.modules.models.Video
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
import com.google.android.exoplayer2.Player.STATE_ENDED

class VideoListAdapter(private val moveToNextVideo: (Int) -> Unit) :
    PagingDataAdapter<Video, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.id == newItem.id
        }

    }) {


    inner class VideoVH(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var player: ExoPlayer? = null
        private var mediaItem: MediaItem? = null
        private var isFinished = false

        init {
            if (player != null) {
                player?.playWhenReady = true
            } else {
                player = ExoPlayer.Builder(binding.playerView.context).build()

                player?.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == STATE_ENDED) {
                            isFinished = true
                            moveToNextVideo(
                                absoluteAdapterPosition,
                            )
                        }
                        super.onPlaybackStateChanged(playbackState)
                    }
                })
                binding.playerView.player = player
            }
        }

        fun bindData(item: Video?) {
            item?.uri?.let {
                mediaItem = MediaItem.fromUri(it)
                player?.setMediaItem(mediaItem!!)
                player?.prepare()
                player?.playWhenReady = true
            }
        }

        fun resumePlayer() {
            if (isFinished) {
                mediaItem?.let { player?.setMediaItem(it) }
                player?.prepare()
            }
            player?.playWhenReady = true
        }

        fun pausePlayer() {
            player?.playWhenReady = false
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VideoVH).bindData(getItem(position))
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as VideoVH).pausePlayer()
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        (holder as VideoVH).resumePlayer()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VideoVH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_video,
                parent,
                false
            )
        )
    }
}