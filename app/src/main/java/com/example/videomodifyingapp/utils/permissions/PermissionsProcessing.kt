package com.example.videomodifyingapp.utils.permissions

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.isPermissionsGranted(permissionList: Array<String>): Boolean {
    return ContextCompat.checkSelfPermission(
        requireContext(),
        permissionList.toString()
    ) == PackageManager.PERMISSION_GRANTED
}