package com.example.videomodifyingapp.ui.videoViewerScreen

import android.content.ContentResolver
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videomodifyingapp.data.local_storage_repository.LocalStorageRepository
import com.example.videomodifyingapp.utils.video_editor.VideoEditor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class VideoViewerViewModel(
    private val videoEditor: VideoEditor,
    private val localStorageRepository: LocalStorageRepository
) : ViewModel() {

    private val _state = MutableStateFlow<VideoViewerScreenState>(VideoViewerScreenState.Idle)
    val state = _state.asStateFlow()

    private val tempFileDir = File("${Environment.getExternalStorageDirectory()}/my_video")

    private val outPutFilePartOne = File(tempFileDir.path, "OutPutFilePartOne.mp4")
    private val outPutFilePartTwo = File(tempFileDir.path, "OutPutFilePartTwo.mp4")
    private val outPutFileFinal = File(tempFileDir.path, "OutPutFilePartFinal.mp4")

    private val tempFile = File(tempFileDir.path, "inPutFile.mp4")

    init {
        viewModelScope.launch {
            videoEditor.isMergingFinished.collect { codeResult ->
                if (codeResult == 0) {
                    _state.update { VideoViewerScreenState.Success }
                } else if (codeResult > 0) {
                    _state.update { VideoViewerScreenState.Error("Something went wrong") }
                }
            }
        }
        viewModelScope.launch {
            videoEditor.isTrimeringFinished.collect { codeResult ->
                if (codeResult == 0) {
                    videoEditor.mergeVideosReversely(
                        outPutFilePartOne,
                        outPutFilePartTwo,
                        outPutFileFinal
                    )
                }
            }
        }
    }

    fun reverseVideoByTwoParts(
        inputVideo: Uri, duration: Int, contentResolver: ContentResolver
    ) {
        _state.update { VideoViewerScreenState.ProcessVideo(outPutFileFinal.path) }
        if (!tempFileDir.exists()) {
            tempFileDir.mkdir()
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (!outPutFileFinal.exists()) {
                trimVideo(
                    inputVideo,
                    0,
                    duration.div(2),
                    contentResolver,
                    tempFile,
                    outPutFilePartOne
                )
                trimVideo(
                    inputVideo,
                    duration.div(2),
                    duration,
                    contentResolver,
                    tempFile,
                    outPutFilePartTwo
                )
            } else {
                _state.update { VideoViewerScreenState.ProcessVideo(outPutFileFinal.path) }
                _state.update { VideoViewerScreenState.Success }
            }
        }
    }

    private suspend fun trimVideo(
        inputVideo: Uri,
        trimFrom: Int,
        trimTo: Int,
        contentResolver: ContentResolver,
        convertorFile: File,
        outputFile: File
    ) {
        localStorageRepository.convertFile(contentResolver, inputVideo, convertorFile)
        videoEditor.trimVideo(
            convertorFile.path,
            outputFile,
            getTime(trimFrom / 1000),
            getTime(trimTo / 1000)
        )
    }

    private fun getTime(seconds: Int): String {
        val hr = seconds / 3600
        val rem = seconds % 3600
        val mn = rem / 60
        val sec = rem % 60
        return String.format("%02d", hr) + ":" + String.format(
            "%02d",
            mn
        ) + ":" + String.format("%02d", sec)
    }

    fun clearFolder() {
        tempFileDir.listFiles()?.forEach { file ->
            file.delete()
        }
        tempFileDir.delete()
    }
}