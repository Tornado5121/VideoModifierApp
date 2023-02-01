package com.example.videomodifyingapp.utils.video_editor

import android.net.Uri
import java.io.File

interface VideoEditor {

    suspend fun mergeVideosReversely(
        partOneVideo: File,
        partTwoVideo: File,
        outputFileUri: File
    )

    suspend fun trimVideo(
        inputFile: Uri,
        outputFilePath: File,
        timeFrom: String,
        timeTo: String
    )
}