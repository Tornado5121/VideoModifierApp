package com.example.videomodifyingapp.ui.videoListScreen

import com.example.videomodifyingapp.models.LocalStorageVideo

sealed class VideoListScreenState {
    data class Success(val videoList: List<LocalStorageVideo>) : VideoListScreenState()
    object Progress : VideoListScreenState()
    object Error : VideoListScreenState()
}