package com.muhammed.aras.presentation.common.helper

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.muhammed.aras.R
import com.muhammed.aras.util.showMessage
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class PermissionHelper @Inject constructor(
    private val fragment: Fragment,
    private val dialogHelper: DialogHelper
) {
    private var onGranted: (() -> Unit)? = null
    private val requestPermissionLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onGranted?.invoke()
        } else {
            showPermissionRationale()
        }
    }

    fun requestCameraPermission(onGranted: () -> Unit) {
        this.onGranted = onGranted

        if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            showPermissionRationale()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun showPermissionRationale() {
        dialogHelper.showDialog(
            title = fragment.getString(R.string.camera_permission_needed),
            message = fragment.getString(R.string.camera_permission_message),
            positiveButtonText = fragment.getString(R.string.go_to_settings),
            negativeButtonText = fragment.getString(R.string.cancel),
            onPositiveButtonClick = { navigateToAppSettings() },
            onNegativeButtonClick = { notifyPermissionNeeded() }
        )
    }

    private fun navigateToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", fragment.requireActivity().packageName, null)
        }
        fragment.startActivity(intent)
    }

    private fun notifyPermissionNeeded() {
        fragment.context?.showMessage("Camera permission is needed to take photos.")
    }
}
