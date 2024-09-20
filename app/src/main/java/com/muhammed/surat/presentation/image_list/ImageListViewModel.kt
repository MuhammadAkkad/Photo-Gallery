package com.muhammed.surat.presentation.image_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammed.surat.data.enums.SortType
import com.muhammed.surat.data.model.FilterModel
import com.muhammed.surat.data.model.PhotoModel
import com.muhammed.surat.domain.repository.PhotoRepository
import com.muhammed.surat.util.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ImageListViewModel @Inject constructor(
    private val repository: PhotoRepository
) : ViewModel() {

    var filterModel = FilterModel()

    var sortType = SortType.NAME

    private val _photos = MutableStateFlow<List<PhotoModel>?>(null)
    val photos: StateFlow<List<PhotoModel>?> get() = _photos

    init {
        fetchPhotos()
    }

    fun setFilter(filter: FilterModel) {
        this.filterModel = filter
        fetchPhotos()
    }

    fun sort(sortType: SortType) {
        this.sortType = sortType
        when (sortType) {
            SortType.NAME -> _photos.value = photos.value?.sortedBy { it.name }
            SortType.DATE -> _photos.value = photos.value?.sortedBy { it.dateTime?.toDate() }
        }
    }

    fun addPhoto(photo: PhotoModel) {
        viewModelScope.launch {
            repository.insertPhoto(photo)
        }
    }

    private fun fetchPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPhotos().collectLatest { photosList ->
                val filteredList = filterList(photosList)
                val sortedList = sortList(filteredList)
                _photos.value = sortedList
            }
        }
    }

    private fun filterList(photosList: List<PhotoModel>): List<PhotoModel> {
        return photosList.filter { photo ->
            val photoDate = photo.dateTime?.toDate()

            if (photoDate != null &&
                filterModel.dateRange != null &&
                filterModel.dateRange?.first != null &&
                filterModel.dateRange?.second != null
            ) {
                photoDate in filterModel.dateRange?.first!!..filterModel.dateRange?.second!!
            } else {
                filterModel.query.isNullOrEmpty() || photo.name.contains(
                    filterModel.query!!,
                    ignoreCase = true
                )
            }
        }
    }

    private fun sortList(filteredList: List<PhotoModel>): List<PhotoModel> {
        return when (sortType) {
            SortType.NAME -> filteredList.sortedBy { it.name }
            SortType.DATE -> filteredList.sortedBy { it.dateTime?.toDate() }
        }
    }
}