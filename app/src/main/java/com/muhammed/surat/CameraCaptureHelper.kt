package com.muhammed.surat

import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

context(androidx.fragment.app.Fragment)
class CameraCaptureHelper {

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val photoMetaData = getPhotoMetadata(photoUri)
                onCaptured?.invoke(photoMetaData)
            } else {
                onError?.invoke()
            }
        }

    private var permissionHelper = PermissionHelper()
    private var onCaptured: ((PhotoModel?) -> Unit)? = null
    private var onError: (() -> Unit)? = null

    fun takePhoto(onCaptured: (PhotoModel?) -> Unit, onError: () -> Unit) {
        this@CameraCaptureHelper.onCaptured = onCaptured
        this@CameraCaptureHelper.onError = onError
        permissionHelper.requestCameraPermission(
            onGranted = { takePictureLauncher.launch(photoUri) },
            onDenied = { onError() }
        )
    }

    private fun getPhotoMetadata(photoUri: Uri?): PhotoModel? {
        return try {
            photoUri?.let { uri ->
                uri.path?.let { path ->
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val exif = inputStream?.let { ExifInterface(it) }

                    val orientation = exif?.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                    val orientationDescription = when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> "90 degrees rotated (Portrait)"
                        ExifInterface.ORIENTATION_ROTATE_180 -> "180 degrees rotated (Upside Down)"
                        ExifInterface.ORIENTATION_ROTATE_270 -> "270 degrees rotated (Landscape)"
                        ExifInterface.ORIENTATION_NORMAL -> "Normal (Landscape)"
                        else -> "Unknown orientation"
                    }
                    val dateTime = exif?.getAttribute(ExifInterface.TAG_DATETIME)
                    val lat = exif?.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                    val long = exif?.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
                    val exposureTime = exif?.getAttribute(ExifInterface.TAG_EXPOSURE_TIME)
                    val make = exif?.getAttribute(ExifInterface.TAG_MAKE)
                    val model = exif?.getAttribute(ExifInterface.TAG_MODEL)

                    val file = File(path)
                    val fileName = file.name

                    PhotoModel(
                        id = System.currentTimeMillis(),
                        name = fileName,
                        uri = photoUri,
                        orientation = orientationDescription,
                        dateTime = dateTime?.formatDate(),
                        latLong = "${lat.hyphenIfEmpty()}, ${long.hyphenIfEmpty()}",
                        exposureTime = exposureTime,
                        cameraMake = make,
                        cameraModel = model
                    )
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private val photoUri: Uri? by lazy {
        try {
            val imageFile = createImageFile()
            FileProvider.getUriForFile(
                requireContext(),
                "${requireActivity().packageName}.fileProvider",
                imageFile
            )
        } catch (e: Exception) {
            requireContext().showMessage("Error creating photo URI")
            null
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "photo_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
}