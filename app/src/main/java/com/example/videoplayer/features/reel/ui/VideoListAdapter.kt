package com.example.videoplayer.features.reel.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.eventFlow
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayer.R
import com.example.videoplayer.domain.entities.Video
import com.example.videoplayer.databinding.ItemComposeRootBinding
import kotlinx.coroutines.launch

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

    @OptIn(UnstableApi::class)
    @Composable
    fun MediaPlayerView(player: ExoPlayer) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    this.player = player
                    this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            }
        )
    }

    @Composable
    fun CustomMediaController(player: ExoPlayer) {
        val isPlaying = player.isPlaying
        Image(
            modifier = Modifier.clickable {
                if (isPlaying) {
                    player.pause()
                } else {
                    player.play()
                }
            },
            painter = painterResource(
                id = if (isPlaying) {
                    R.drawable.ic_pause
                } else {
                    R.drawable.ic_play
                }
            ),
            contentDescription = if (isPlaying) "Pause" else "Play"
        )
    }

    @Composable
    fun PlayerWithController(player: ExoPlayer) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MediaPlayerView(player = player)

            Spacer(modifier = Modifier.height(16.dp))

            CustomMediaController(player = player)
        }
    }

    inner class VideoVHCompose(private val binding: ItemComposeRootBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var player: ExoPlayer? = null
        private var mediaItem: MediaItem? = null
        private var isFinished = false

        fun bindData(item: Video?) {
            binding.composeView.setContent {
                val context = LocalContext.current
                player = remember {
                    ExoPlayer.Builder(context).build().apply {
                        addListener(object : Player.Listener {
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
                    }
                }

                item?.urlString?.let {
                    mediaItem = MediaItem
                        .Builder()
                        .setUri(it)
                        .build()
                    player?.setMediaItem(mediaItem!!)
                    player?.prepare()
                    player?.playWhenReady = true
                }

                player?.let {
                    PlayerWithController(it)
                }

                val lifecycleOwner = LocalLifecycleOwner.current
                val coRoutineContext = rememberCoroutineScope()

                LaunchedEffect(lifecycleOwner) {
                    coRoutineContext.launch {
                        lifecycleOwner.lifecycle.eventFlow.collect {
                            when (it) {
                                Lifecycle.Event.ON_RESUME -> {
                                    resumePlayer()
                                }

                                Lifecycle.Event.ON_PAUSE -> {
                                    pausePlayer()
                                }

                                else -> {}
                            }
                        }
                    }
                }

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
        (holder as VideoVHCompose).bindData(getItem(position))
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as VideoVHCompose).pausePlayer()
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        (holder as VideoVHCompose).resumePlayer()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VideoVHCompose(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_compose_root,
                parent,
                false
            )
        )
    }
}