package com.example.videomodifyingapp.utils.my_player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class MyPlayer(
    private val context: Context
) {

    private var _myPlayer: ExoPlayer? = ExoPlayer.Builder(context)
        .setLoadControl(
            DefaultLoadControl.Builder()
                .setBufferDurationsMs(5000, 10000, 1000, 1000)
                .build()
        )
        .build()

    private val myPlayer: ExoPlayer
        get() = _myPlayer!!

    fun getPlayerInstance(): ExoPlayer {
        return myPlayer
    }

    fun playVideoByUri(videoUri: Uri) {
        val videoItem = MediaItem.fromUri(videoUri)
        myPlayer.addMediaItem(videoItem)
        myPlayer.prepare()
        myPlayer.playWhenReady = true
    }

    fun clearResources() {
        myPlayer.stop()
        myPlayer.clearMediaItems()
        myPlayer.release()
        _myPlayer = null
    }
}