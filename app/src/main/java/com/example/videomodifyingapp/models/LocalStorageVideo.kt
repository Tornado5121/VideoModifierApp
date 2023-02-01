package com.example.videomodifyingapp.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalStorageVideo(
    val uri: Uri,
    val name: String,
    val duration: Int?,
): Parcelable