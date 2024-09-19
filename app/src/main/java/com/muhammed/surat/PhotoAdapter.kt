package com.muhammed.surat

import android.util.Log
import android.widget.Filter
import android.widget.Filterable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muhammed.surat.databinding.ItemPhotoBinding
import java.util.Date

class PhotoAdapter(
    private val onPhotoClick: (PhotoModel) -> Unit,
    private val getDateRange: () -> Pair<Date?, Date?>?,
) : ListAdapter<PhotoModel, PhotoAdapter.PhotoViewHolder>(PhotoDiffCallback()), Filterable {

    private var originalList: List<PhotoModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position), onPhotoClick)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.trim()
                val filterResults = FilterResults()
                val (startDate, endDate) = getDateRange() ?: Pair(null, null)

                filterResults.values = originalList.filter { photo ->
                    val photoDate = photo.dateTime?.toDate()
                    val matchesDate =
                        startDate != null && endDate != null && photoDate!! in (startDate..endDate)
                    val matchesQuery =
                        query.isNullOrEmpty() || photo.name.contains(query, ignoreCase = true)

                    (startDate != null && endDate != null && matchesDate) || matchesQuery
                }

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as List<PhotoModel>?)
            }
        }
    }

    fun setItems(list: List<PhotoModel>) {
        originalList = list
        submitList(list)
    }

    class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photoItem: PhotoModel, onPhotoClick: (PhotoModel) -> Unit) {
            with(binding) {
                imageViewPhoto.load(photoItem.uri)
                root.onClick {
                    onPhotoClick(photoItem)
                }
            }
        }
    }

    class PhotoDiffCallback : DiffUtil.ItemCallback<PhotoModel>() {
        override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
            return oldItem == newItem
        }
    }
}