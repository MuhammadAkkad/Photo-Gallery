package com.muhammed.surat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImageListViewModel(private val repository: PhotoRepository) : ViewModel() {

    private val _photos = MutableStateFlow<List<PhotoModel>>(emptyList())
    val photos: StateFlow<List<PhotoModel>> get() = _photos

    init {
        viewModelScope.launch {
            repository.getAllPhotos().collect { photosList ->
                _photos.value = photosList
            }
        }
    }

    fun addPhoto(photo: PhotoModel) {
        viewModelScope.launch {
            repository.insertPhoto(photo)
        }
    }
}