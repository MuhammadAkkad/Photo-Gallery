package com.muhammed.surat.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.muhammed.surat.data.dao.PhotoDao
import com.muhammed.surat.data.model.PhotoModel

@Database(entities = [PhotoModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}
