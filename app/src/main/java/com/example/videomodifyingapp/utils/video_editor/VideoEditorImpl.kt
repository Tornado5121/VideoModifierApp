package com.example.videomodifyingapp.utils.video_editor

import com.arthenica.mobileffmpeg.FFmpeg
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File

class VideoEditorImpl : VideoEditor {

    private val _isMergingFinished = MutableStateFlow(-1)
    override val isMergingFinished = _isMergingFinished.asStateFlow()

    private val _isTrimeringFinished = MutableStateFlow(-1)
    override val isTrimeringFinished = _isTrimeringFinished.asStateFlow()

    override suspend fun mergeVideosReversely(
        partOneVideo: File,
        partTwoVideo: File,
        outputFileUri: File
    ) {
        val command =
            arrayOf(
                "-i",
                partTwoVideo.path,
                "-i",
                partOneVideo.path,
                "-filter_complex",
                "[0:v]fps=25,format=yuv420p,setpts=PTS-STARTPTS[v0];[0:a]aformat=sample_rates=44100:channel_layouts=stereo,asetpts=PTS-STARTPTS[a0];[1:v]fps=25,format=yuv420p,setpts=PTS-STARTPTS[v1];[1:a]aformat=sample_rates=44100:channel_layouts=stereo,asetpts=PTS-STARTPTS[a1];[v0][a0][v1][a1]concat=n=2:v=1:a=1",
                "-movflags",
                "+faststart",
                outputFileUri.path
            )
        FFmpeg.executeAsync(command) { _, result ->
            if (result >= 0) {
                _isMergingFinished.update { result }
                _isMergingFinished.update { -1 }
            }
        }
    }

    override suspend fun trimVideo(
        inputFile: String,
        outputFilePath: File,
        timeFrom: String,
        timeTo: String
    ) {
        val command = arrayOf(
            "-i",
            inputFile,
            "-ss",
            timeFrom,
            "-to",
            timeTo,
            "-c",
            "copy",
            outputFilePath.path
        )
        FFmpeg.executeAsync(command) { _, result ->
            if (result >= 0) {
                _isTrimeringFinished.update { result }
                _isTrimeringFinished.update { -1 }
            }
        }
    }
}