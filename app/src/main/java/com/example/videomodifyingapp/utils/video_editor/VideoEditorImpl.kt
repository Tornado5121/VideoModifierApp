package com.example.videomodifyingapp.utils.video_editor

import android.net.Uri
import com.arthenica.mobileffmpeg.FFmpeg
import java.io.File

class VideoEditorImpl : VideoEditor {

    override suspend fun mergeVideosReversely(
        partOneVideo: File,
        partTwoVideo: File,
        outputFileUri: File
    ) {
        val command =
            arrayOf(
                "-f",
                "concat",
                "-i",
                partTwoVideo.path,
                "-i",
                partOneVideo.path,
                "-c",
                "copy",
                outputFileUri.path
            )
        FFmpeg.executeAsync(command) { _, _ -> }
    }

    override suspend fun trimVideo(
        inputFile: Uri,
        outputFilePath: File,
        timeFrom: String,
        timeTo: String
    ) {
        val command = arrayOf(
            "-i",
            inputFile.toString(),
            "-ss",
            timeFrom,
            "-to",
            timeTo,
            "-c:v",
            "copy",
            "-c:a",
            "copy",
            outputFilePath.path
        )
        FFmpeg.executeAsync(command) { _, _ -> }
    }
}