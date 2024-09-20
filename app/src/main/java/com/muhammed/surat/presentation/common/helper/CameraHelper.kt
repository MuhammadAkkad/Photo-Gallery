package com.muhammed.surat.presentation.common.helper

import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import com.muhammed.surat.R
import com.muhammed.surat.data.model.PhotoModel
import com.muhammed.surat.util.hyphenIfEmpty
import com.muhammed.surat.util.showMessage
import dagger.hilt.android.scopes.FragmentScoped
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject

@FragmentScoped
class CameraHelper @Inject constructor(
    private val fragment: Fragment,
    private val permissionHelper: PermissionHelper
) {
    private var photoUri: Uri? = null
    private val storageDir = fragment.context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    private var onCaptured: ((PhotoModel?) -> Unit)? = null
    private var onError: (() -> Unit)? = null
    private val takePictureLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val photoMetaData = getPhotoMetadata(photoUri)
                onCaptured?.invoke(photoMetaData)
            } else {
                onError?.invoke()
            }
        }

    fun takePhoto(onCaptured: (PhotoModel?) -> Unit, onError: () -> Unit) {
        this.onCaptured = onCaptured
        this.onError = onError
        permissionHelper.requestCameraPermission(
            onGranted = { startPhotoCapture() }
        )
    }

    private fun startPhotoCapture() {
        photoUri = createPhotoUri()
        photoUri?.let { uri ->
            takePictureLauncher.launch(uri)
        } ?: onError?.invoke()
    }

    private fun createPhotoUri(): Uri? {
        return try {
            val timeStamp = System.currentTimeMillis()
            val fileName = "photo_${timeStamp}.jpg"
            val imageFile = File(storageDir, fileName)
            fragment.context?.let { context ->
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileProvider",
                    imageFile
                )
            }
        } catch (e: Exception) {
            fragment.context?.showMessage(fragment.getString(R.string.error_creating_photo_uri))
            null
        }
    }

    private fun getPhotoMetadata(photoUri: Uri?): PhotoModel? {
        return photoUri?.let { uri ->
            var inputStream: InputStream? = null
            try {
                val path = uri.path ?: return@let null
                inputStream = fragment.context?.contentResolver?.openInputStream(uri)
                val exif = inputStream?.let { ExifInterface(it) }
                PhotoModel(
                    id = UUID.randomUUID().toString(),
                    name = File(path).name,
                    uri = photoUri,
                    orientation = getOrientationDescription(exif),
                    dateTime = exif?.getAttribute(ExifInterface.TAG_DATETIME),
                    latLong = "${
                        exif?.getAttribute(ExifInterface.TAG_GPS_LATITUDE)?.hyphenIfEmpty()
                    }, ${exif?.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)?.hyphenIfEmpty()}",
                    exposureTime = exif?.getAttribute(ExifInterface.TAG_EXPOSURE_TIME),
                    cameraMake = exif?.getAttribute(ExifInterface.TAG_MAKE),
                    cameraModel = exif?.getAttribute(ExifInterface.TAG_MODEL)
                )

            } catch (e: IOException) {
                e.printStackTrace()
                null
            } finally {
                inputStream?.close()
            }
        }
    }

    private fun getOrientationDescription(exif: ExifInterface?): String {
        val orientation = exif?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> fragment.getString(R.string.orientation_90)
            ExifInterface.ORIENTATION_ROTATE_180 -> fragment.getString(R.string.orientation_180)
            ExifInterface.ORIENTATION_ROTATE_270 -> fragment.getString(R.string.orientation_270)
            ExifInterface.ORIENTATION_NORMAL -> fragment.getString(R.string.orientation_normal)
            else -> fragment.getString(R.string.orientation_unknown)
        }
    }
}
