package com.example.videoplayer.features.reel.ui

import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.databinding.DataBindingUtil
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayer.R
import com.example.videoplayer.databinding.ItemVideoBinding
import com.example.videoplayer.data.local.entities.Video
import com.example.videoplayer.databinding.ItemComposeRootBinding

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


    inner class VideoVH(binding: ItemVideoBinding) :
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

    @Composable
    fun MediaPlayerView(player: ExoPlayer) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    this.player = player
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
    fun PlayerWithController() {
        val player: ExoPlayer = ExoPlayer.Builder(LocalContext.current).build()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            MediaPlayerView(player = player) // This will display the video

            Spacer(modifier = Modifier.height(16.dp))

            CustomMediaController(player = player) // This will display custom controls
        }
    }

    inner class VideoVHCompose(binding: ItemComposeRootBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var player: ExoPlayer? = null
        private var mediaItem: MediaItem? = null
        private var isFinished = false

        init {

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
                        prepare()
                    }
                }

                // Ensure player is released when the screen is destroyed
                DisposableEffect(player) {
                    onDispose {
                        player?.release()
                    }
                }

                player?.let {
                    PlayerWithController()
                }
//                    VideoItemComposable(video = video, onVideoCompleted = { onVideoCompleted(adapterPosition) })
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

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VideoVHCompose).bindData(getItem(position))
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
//        (holder as VideoVHCompose).pausePlayer()
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
//        (holder as VideoVHCompose).resumePlayer()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return VideoVH(
//            DataBindingUtil.inflate(
//                LayoutInflater.from(parent.context),
//                R.layout.item_video,
//                parent,
//                false
//            )
//        )
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