package com.muhammed.surat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert
    suspend fun insertPhoto(photo: PhotoModel)

    @Query("SELECT * FROM photos ORDER BY name ASC")
    fun getPhotosSortedByName(): Flow<List<PhotoModel>>

    @Query("SELECT * FROM photos ORDER BY dateTime ASC")
    fun getPhotosSortedByDate(): Flow<List<PhotoModel>>
}