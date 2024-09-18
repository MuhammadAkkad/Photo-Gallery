package com.muhammed.surat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert
    suspend fun insertPhoto(photo: PhotoModel)

    @Query("SELECT * FROM photos")
    fun getAllPhotos(): Flow<List<PhotoModel>>
}