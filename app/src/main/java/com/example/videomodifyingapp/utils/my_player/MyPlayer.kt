package com.example.videomodifyingapp.utils.my_player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class MyPlayer(
    context: Context
) {

    private var _myPlayer: ExoPlayer? = ExoPlayer.Builder(context)
        .build()

    private val myPlayer: ExoPlayer
        get() = _myPlayer!!

    fun getPlayerInstance(): ExoPlayer {
        return myPlayer
    }

    fun setVideoSource(videoUri: Uri) {
        myPlayer.stop()
        myPlayer.removeMediaItem(0)
        val videoItem = MediaItem.fromUri(videoUri)
        myPlayer.addMediaItem(videoItem)
    }

    fun play() {
        myPlayer.prepare()
        myPlayer.play()
    }

    fun clearResources() {
        myPlayer.stop()
        myPlayer.clearMediaItems()
        myPlayer.release()
        _myPlayer = null
    }
}