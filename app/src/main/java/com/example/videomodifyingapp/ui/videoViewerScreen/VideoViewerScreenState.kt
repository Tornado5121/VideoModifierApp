package com.example.videomodifyingapp.ui.videoViewerScreen

sealed class VideoViewerScreenState {
    object Success : VideoViewerScreenState()
    data class ProcessVideo(val videoUri: String) : VideoViewerScreenState()
    data class Error(val errorMessage: String) : VideoViewerScreenState()
    object Idle: VideoViewerScreenState()
}