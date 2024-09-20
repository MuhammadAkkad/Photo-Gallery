package com.muhammed.surat.presentation.common.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.muhammed.surat.R
import com.muhammed.surat.data.model.PhotoModel
import com.muhammed.surat.util.hyphenIfEmpty

class CustomMetadataView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val metadataContainer: LinearLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_meta_data_view, this, true)
        orientation = VERTICAL
        metadataContainer = findViewById(R.id.metadataContainer)
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

        metadataContainer.removeAllViews()
        data.forEach { (title, value) ->
            val view = LayoutInflater.from(context)
                .inflate(R.layout.custom_meta_item_view, metadataContainer, false)
            val titleTextView: TextView = view.findViewById(R.id.title_text_view)
            val valueTextView: TextView = view.findViewById(R.id.value_text_view)
            titleTextView.text = title
            valueTextView.text = value.hyphenIfEmpty()
            metadataContainer.addView(view)
        }
    }
}
