package com.muhammed.aras.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.muhammed.aras.data.model.PhotoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert
    suspend fun insertPhoto(photo: PhotoModel)

    @Query("SELECT * FROM photos")
    fun fetchPhotos(): Flow<List<PhotoModel>>
}