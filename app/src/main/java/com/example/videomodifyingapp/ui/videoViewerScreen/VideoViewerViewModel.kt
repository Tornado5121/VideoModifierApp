package com.example.videomodifyingapp.ui.videoViewerScreen

import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videomodifyingapp.utils.video_editor.VideoEditor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

class VideoViewerViewModel(
    private val videoEditor: VideoEditor
) : ViewModel() {

    private val outPutFinalFile =
        File(Environment.getExternalStorageDirectory().path + "/MyFfmpegVideos/OutPutFinalFile.mp4")

    private val outPutFilePartOne =
        File(Environment.getExternalStorageDirectory().path + "/MyFfmpegVideos/OutPutFilePartOne.mp4")

    private val outPutFilePartTwo =
        File(Environment.getExternalStorageDirectory().path + "/MyFfmpegVideos/OutPutFilePartTwo.mp4")

    private val _reversedVideoUri = MutableSharedFlow<String>()
    val reversedVideoUri = _reversedVideoUri.asSharedFlow()

    fun reverseVideoByTwoParts(
        inputVideo: Uri, duration: Int?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            videoEditor.trimVideo(
                inputVideo,
                outPutFilePartOne,
                0.toString(),
                (duration?.div(2)).toString()
            )
            videoEditor.trimVideo(
                inputVideo,
                outPutFilePartTwo,
                (duration?.div(2)).toString(),
                duration.toString()
            )
            videoEditor.mergeVideosReversely(
                outPutFilePartOne,
                outPutFilePartTwo,
                outPutFinalFile
            )
            _reversedVideoUri.emit(outPutFinalFile.path)
        }
    }
}