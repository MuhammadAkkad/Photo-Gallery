package com.muhammed.surat

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val photoDao: PhotoDao
) {

    fun getPhotosSortedByName(): Flow<List<PhotoModel>> = photoDao.getPhotosSortedByName()
    fun getPhotosSortedByDate(): Flow<List<PhotoModel>> = photoDao.getPhotosSortedByDate()

    suspend fun insertPhoto(photo: PhotoModel) { photoDao.insertPhoto(photo) }
}