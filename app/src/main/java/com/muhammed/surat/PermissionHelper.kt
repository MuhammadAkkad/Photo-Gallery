package com.muhammed.surat

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

context(Fragment)
class PermissionHelper {

    private var requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onGranted?.invoke()
        } else {
            onDenied?.invoke()
        }
    }
    private var dialogHelper: DialogHelper? = null
    private var onGranted: (() -> Unit)? = null
    private var onDenied: (() -> Unit)? = null

    fun requestCameraPermission(onGranted: () -> Unit, onDenied: () -> Unit) {
        this@PermissionHelper.onGranted = onGranted
        this@PermissionHelper.onDenied = onDenied
        when {
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                dialogHelper = DialogHelper("Camera Permission Needed",
                    "We need camera access to capture photos. Please enable it in settings.",
                    "Go to Settings",
                    "Cancel",
                    onPositiveButtonClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", requireActivity().packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    },
                    onNegativeButtonClick = {
                        requireContext().showMessage("Camera permission is needed to take photos.")
                    })
                dialogHelper?.showDialog()
            }

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}