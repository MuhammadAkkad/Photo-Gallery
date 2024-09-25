package com.muhammed.aras.presentation.image_detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.muhammed.aras.data.model.PhotoModel
import com.muhammed.aras.databinding.FragmentImageBinding
import com.muhammed.aras.presentation.base.BaseFragment
import com.muhammed.aras.util.load
import com.muhammed.aras.util.onClick
import com.muhammed.aras.util.toggleFullScreen
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
