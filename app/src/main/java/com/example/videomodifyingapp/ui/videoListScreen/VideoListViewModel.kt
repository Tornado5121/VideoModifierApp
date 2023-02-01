package com.example.videomodifyingapp.ui.videoListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videomodifyingapp.data.local_storage_repository.LocalStorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VideoListViewModel(
    private val localStorageRepository: LocalStorageRepository
) : ViewModel() {
    private val _screenState = MutableStateFlow<VideoListScreenState>(VideoListScreenState.Progress)
    val screenState = _screenState.asStateFlow()

    init {
        loadVideoList()
    }

    fun loadVideoList() {
        viewModelScope.launch {
            _screenState.update { VideoListScreenState.Progress }
            _screenState.update {
                VideoListScreenState.Success(
                    localStorageRepository.getLocalVideosList()
                )
            }
        }
    }
}