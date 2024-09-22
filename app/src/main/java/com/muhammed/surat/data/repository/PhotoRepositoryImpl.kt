package com.muhammed.surat.data.repository

import com.muhammed.surat.data.dao.PhotoDao
import com.muhammed.surat.data.model.PhotoModel
import com.muhammed.surat.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val photoDao: PhotoDao
) : PhotoRepository {

    override suspend fun insertPhoto(photo: PhotoModel) {
        photoDao.insertPhoto(photo)
    }

    override suspend fun fetchPhotos(): Flow<List<PhotoModel>> {
        return photoDao.fetchPhotos()
    }
}