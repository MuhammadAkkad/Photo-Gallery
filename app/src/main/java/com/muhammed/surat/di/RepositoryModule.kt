package com.muhammed.surat.di

import com.muhammed.surat.data.dao.PhotoDao
import com.muhammed.surat.data.repository.PhotoRepositoryImpl
import com.muhammed.surat.domain.repository.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePhotoRepository(photoDao: PhotoDao): PhotoRepository {
        return PhotoRepositoryImpl(photoDao)
    }
}