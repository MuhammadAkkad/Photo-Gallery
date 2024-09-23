package com.muhammed.surat.presentation.image_detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.muhammed.surat.data.model.PhotoModel
import com.muhammed.surat.databinding.FragmentImageBinding
import com.muhammed.surat.presentation.base.BaseFragment
import com.muhammed.surat.util.load
import com.muhammed.surat.util.onClick
import com.muhammed.surat.util.toggleFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageFragment : BaseFragment<FragmentImageBinding>() {

    private var photo: PhotoModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photo = arguments?.let { ImageFragmentArgs.fromBundle(it).photo }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            metaDataView.setMetadata(photo)
            image.load(photo?.uri)
        }
    }

    override fun onResume() {
        super.onResume()
        toggleFullScreen(true)
    }

    override fun onPause() {
        super.onPause()
        toggleFullScreen(false)
    }

    override fun initListeners() {
        with(binding) {
            btnBack.onClick {
                navigateUp()
            }
            image.onClick {
                metaDataView.isVisible = metaDataView.isVisible.not()
                btnBack.isVisible = btnBack.isVisible.not()
            }
        }
    }
}
