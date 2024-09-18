package com.muhammed.surat

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val photoDao: PhotoDao
) {
    fun getAllPhotos(): Flow<List<PhotoModel>> = photoDao.getAllPhotos()

    suspend fun insertPhoto(photo: PhotoModel) {
        photoDao.insertPhoto(photo)
    }
}