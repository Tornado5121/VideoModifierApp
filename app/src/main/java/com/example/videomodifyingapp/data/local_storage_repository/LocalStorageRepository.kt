package com.example.videomodifyingapp.data.local_storage_repository

import com.example.videomodifyingapp.models.LocalStorageVideo

interface LocalStorageRepository {

    fun getLocalVideosList() : List<LocalStorageVideo>
}