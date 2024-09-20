package com.muhammed.surat.presentation.image_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muhammed.surat.data.model.PhotoModel
import com.muhammed.surat.databinding.ItemPhotoBinding
import com.muhammed.surat.util.load
import com.muhammed.surat.util.onClick

class PhotoAdapter(
    private val onPhotoClick: (PhotoModel) -> Unit,
) : ListAdapter<PhotoModel, PhotoAdapter.PhotoViewHolder>(PhotoDiffCallback()) {

    fun setItems(list: List<PhotoModel>) {
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position), onPhotoClick)
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