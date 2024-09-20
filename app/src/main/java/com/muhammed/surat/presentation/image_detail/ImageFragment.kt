package com.muhammed.surat.presentation.image_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.muhammed.surat.data.model.PhotoModel
import com.muhammed.surat.databinding.FragmentImageBinding
import com.muhammed.surat.util.load
import com.muhammed.surat.util.onClick
import com.muhammed.surat.util.toggleFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    private var photo: PhotoModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photo = arguments?.let { ImageFragmentArgs.fromBundle(it).photo }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            initListeners()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        with(binding) {
            btnBack.onClick {
                findNavController().navigateUp()
            }
            image.onClick {
                metaDataView.isVisible = metaDataView.isVisible.not()
                btnBack.isVisible = btnBack.isVisible.not()
            }
        }
    }
}
