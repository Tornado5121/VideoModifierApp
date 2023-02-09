package com.example.videomodifyingapp.utils.video_editor

import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface VideoEditor {

    val isMergingFinished: StateFlow<Int>
    val isTrimeringFinished: StateFlow<Int>
    suspend fun mergeVideosReversely(
        partOneVideo: File,
        partTwoVideo: File,
        outputFileUri: File
    )

    suspend fun trimVideo(
        inputFile: String,
        outputFilePath: File,
        timeFrom: String,
        timeTo: String
    )
}