package com.muhammed.surat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.muhammed.surat.databinding.FragmentImageBinding

class ImageFragment : Fragment() {

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photo = arguments?.let { ImageFragmentArgs.fromBundle(it).imageUrl }

        with(binding) {
            image.apply {
                load(photo?.uri)
                onClick {
                    metaDataView.isVisible = metaDataView.isVisible.not()
                }
            }

            val metadata = mapOf(
                "Name: " to photo?.name,
                "Orientation: " to photo?.orientation,
                "Date and Time: " to photo?.dateTime,
                "Latitude, Longitude: " to photo?.latLong,
                "Exposure Time: " to photo?.exposureTime,
                "Camera Make: " to photo?.cameraMake,
                "Camera Model: " to photo?.cameraModel
            )
            metaDataView.setMetadata(metadata)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
