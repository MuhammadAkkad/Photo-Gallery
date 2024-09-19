package com.muhammed.surat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ImageListViewModel @Inject constructor(
    private val repository: PhotoRepository
) : ViewModel() {

    private val _photos = MutableStateFlow<List<PhotoModel>?>(null)
    val photos: StateFlow<List<PhotoModel>?> get() = _photos

    init {
        getPhotosSortedByName()
    }

    fun sortPhotos(type: SortType) {
        when (type) {
            SortType.NAME -> getPhotosSortedByName()
            SortType.DATE -> getPhotosSortedByDate()
        }
    }

    fun addPhoto(photo: PhotoModel) {
        viewModelScope.launch {
            repository.insertPhoto(photo)
        }
    }

    private fun getPhotosSortedByName() {
        viewModelScope.launch {
            repository.getPhotosSortedByName().collectLatest { photosList ->
                _photos.value = photosList
            }
        }
    }

    private fun getPhotosSortedByDate() {
        viewModelScope.launch {
            repository.getPhotosSortedByDate().collectLatest { photosList ->
                _photos.value = photosList
            }
        }
    }
}