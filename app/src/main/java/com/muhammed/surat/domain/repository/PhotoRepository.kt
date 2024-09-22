package com.muhammed.surat.domain.repository

import com.muhammed.surat.data.model.PhotoModel
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {

    suspend fun fetchPhotos(): Flow<List<PhotoModel>>

    suspend fun insertPhoto(photo: PhotoModel)
}
