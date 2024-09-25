package com.muhammed.aras.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.muhammed.aras.data.dao.PhotoDao
import com.muhammed.aras.data.model.PhotoModel

@Database(entities = [PhotoModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}
