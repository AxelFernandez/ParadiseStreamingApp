package com.axelfernandez.streaming.ui.videoStreaming

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView

class StreamViewModel() : ViewModel() {
    var player: SimpleExoPlayer? = null
    var playbackPosition: Long? = null
    var playerView: StyledPlayerView? = null


    // https://videohd.live:19360/8056/8056.m3u8
    // http://sonic1.veemesoft.com.ar:8056/live
    fun initializePlayer(context: Context) {
        player = SimpleExoPlayer.Builder(context).build()
        playerView?.player = player
        val mediaItem: MediaItem = MediaItem.Builder()
            .setUri("https://videohd.live:19360/8056/8056.m3u8")
            .build()
        player?.setMediaItem(mediaItem)
        player?.addListener(ComponentListener())
    }

    fun releasePlayer() {

        val player = player ?: return
        playbackPosition = player.getCurrentPosition();
        player.removeListener(ComponentListener());
        player.release();
        this.player = null;

    }

    fun startPlayer() {
        player?.prepare()
        player?.play();

    }

    private class ComponentListener : Player.EventListener {
        override fun onPlaybackStateChanged(state: Int) {
            super.onPlaybackStateChanged(state)
            Log.e("ERROR", state.toString())

        }


        override fun onPlayerError(error: ExoPlaybackException) {
            super.onPlayerError(error)
            when (error.type) {
                (ExoPlaybackException.TYPE_SOURCE) -> {
                    Log.e("ERROR", error.toString())

                }
            }

        }
    }
}