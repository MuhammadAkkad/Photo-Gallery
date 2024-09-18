package com.muhammed.surat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView

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

    fun setMetadata(metadata: Map<String, String?>) {
        metadataContainer.removeAllViews()

        metadata.forEach { (title, value) ->
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
