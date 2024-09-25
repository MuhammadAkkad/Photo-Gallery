package com.muhammed.aras.di

import com.muhammed.aras.data.dao.PhotoDao
import com.muhammed.aras.data.repository.PhotoRepositoryImpl
import com.muhammed.aras.domain.repository.PhotoRepository
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