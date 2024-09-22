package com.muhammed.surat.presentation.common.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.muhammed.surat.R
import com.muhammed.surat.data.model.PhotoModel
import com.muhammed.surat.databinding.MetaDataComponentViewBinding
import com.muhammed.surat.util.hyphenIfEmpty

class MetaDataComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var _binding: MetaDataComponentViewBinding? = null
        get() {
            _binding = field ?: inflateBinding()
            return field
        }

    private val binding: MetaDataComponentViewBinding
        get() = _binding ?: throw IllegalStateException("Binding is not initialized")

    private fun inflateBinding(): MetaDataComponentViewBinding {
        return MetaDataComponentViewBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setMetadata(photo: PhotoModel?) {

        val data = mapOf(
            "Name: " to photo?.name,
            "Orientation: " to photo?.orientation,
            "Date and Time: " to photo?.dateTime,
            "Latitude, Longitude: " to photo?.latLong,
            "Exposure Time: " to photo?.exposureTime,
            "Camera Make: " to photo?.cameraMake,
            "Camera Model: " to photo?.cameraModel
        )

        binding.metadataContainer.removeAllViews()
        data.forEach { (title, value) ->
            val view = LayoutInflater.from(context)
                .inflate(R.layout.meta_data_item_view, binding.metadataContainer, false)
            val titleTextView: TextView = view.findViewById(R.id.title_text_view)
            val valueTextView: TextView = view.findViewById(R.id.value_text_view)
            titleTextView.text = title
            valueTextView.text = value.hyphenIfEmpty()
            binding.metadataContainer.addView(view)
        }
    }
}
