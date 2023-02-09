package com.example.videomodifyingapp.data.local_storage_repository

import android.content.ContentResolver
import android.net.Uri
import com.example.videomodifyingapp.models.LocalStorageVideo
import java.io.File

interface LocalStorageRepository {

    suspend fun getLocalVideosList(): List<LocalStorageVideo>
    suspend fun convertFile(contentResolver: ContentResolver, inputVideo: Uri, inPutFilePartOne: File)
}