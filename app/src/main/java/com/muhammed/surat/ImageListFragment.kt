package com.muhammed.surat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.muhammed.surat.databinding.FragmentImageListBinding

class ImageListFragment : Fragment() {

    private var _binding: FragmentImageListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PhotoAdapter
    private lateinit var cameraCaptureHelper: CameraCaptureHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraCaptureHelper = CameraCaptureHelper()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btn.setOnClickListener {
            cameraCaptureHelper.takePhoto(
                onCaptured = { photoMeta ->
                    adapter.submitList(listOf(photoMeta))
                },
                onError = {
                requireContext().showMessage("Photo capture failed")
                })
        }

        initList()
    }


    private fun initList() {
        adapter = PhotoAdapter { photo ->
            val action = ImageListFragmentDirections.actionImageListFragmentToImageFragment(photo)
            findNavController().navigate(action)
        }

        binding.recyclerViewPhotos.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@ImageListFragment.adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
