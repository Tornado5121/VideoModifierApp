package com.example.videomodifyingapp.data.local_storage_repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.videomodifyingapp.models.LocalStorageVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class LocalStorageRepositoryImpl(
    private val context: Context
) : LocalStorageRepository {

    @SuppressLint("Recycle")
    override suspend fun convertFile(
        contentResolver: ContentResolver,
        inputVideo: Uri,
        inPutFilePartOne: File
    ) {
        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(inputVideo, "r", null)
        val inputStream =
            withContext(Dispatchers.IO) { FileInputStream(parcelFileDescriptor?.fileDescriptor) }
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(inPutFilePartOne)
        }
        inputStream.copyTo(outputStream)
    }

    override suspend fun getLocalVideosList(): List<LocalStorageVideo> {
        val videoList = mutableListOf<LocalStorageVideo>()

        val collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION
        )

        val query = context.contentResolver.query(
            collection,
            projection,
            null,
            null,
            null
        )
        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                videoList += LocalStorageVideo(contentUri, name, duration)
            }
        }
        return videoList
    }
}