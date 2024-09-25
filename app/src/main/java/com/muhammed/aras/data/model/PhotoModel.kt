package com.muhammed.aras.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "photos")
data class PhotoModel(
    @PrimaryKey val id: String,
    val name: String,
    val uri: Uri?,
    val orientation: String?,
    val dateTime: String?,
    val latLong: String?,
    val exposureTime: String?,
    val cameraMake: String?,
    val cameraModel: String?
) : Serializable