package com.example.videomodifyingapp.di

import com.example.videomodifyingapp.data.local_storage_repository.LocalStorageRepository
import com.example.videomodifyingapp.data.local_storage_repository.LocalStorageRepositoryImpl
import com.example.videomodifyingapp.ui.videoListScreen.VideoListViewModel
import com.example.videomodifyingapp.ui.videoViewerScreen.VideoViewerViewModel
import com.example.videomodifyingapp.utils.my_player.MyPlayer
import com.example.videomodifyingapp.utils.video_editor.VideoEditor
import com.example.videomodifyingapp.utils.video_editor.VideoEditorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single<VideoEditor> { VideoEditorImpl() }
    single<LocalStorageRepository> { LocalStorageRepositoryImpl(androidContext()) }
    single { MyPlayer(androidContext()) }
}

val viewModelModule = module {
    viewModel { VideoListViewModel(get()) }
    viewModel { VideoViewerViewModel(get(), get()) }
}