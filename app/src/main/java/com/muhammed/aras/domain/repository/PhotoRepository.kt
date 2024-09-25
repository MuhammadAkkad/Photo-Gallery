package com.muhammed.aras.domain.repository

import com.muhammed.aras.data.model.PhotoModel
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {

    suspend fun fetchPhotos(): Flow<List<PhotoModel>>

    suspend fun insertPhoto(photo: PhotoModel)
}
