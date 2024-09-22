package com.muhammed.surat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.muhammed.surat.data.model.PhotoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert
    suspend fun insertPhoto(photo: PhotoModel)

    @Query("SELECT * FROM photos")
    fun fetchPhotos(): Flow<List<PhotoModel>>
}