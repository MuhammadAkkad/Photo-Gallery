package com.muhammed.surat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.muhammed.surat.databinding.FragmentImageListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImageListFragment : Fragment() {

    @Inject
    lateinit var cameraCaptureHelper: CameraCaptureHelper
    private val viewModel: ImageListViewModel by viewModels()
    private var _binding: FragmentImageListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PhotoAdapter { photo ->
            val action = ImageListFragmentDirections.actionImageListFragmentToImageFragment(photo)
            findNavController().navigate(action)
        }
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
        initList()
        setupObservers()
        setupListeners()
    }

    private fun initList() {
        binding.recyclerViewPhotos.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = this@ImageListFragment.adapter
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.photos.collect {
                it?.let { photoList ->
                    adapter.submitList(photoList)
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btn.onClick {
            cameraCaptureHelper.takePhoto(
                onCaptured = {
                    it?.let { photoMeta ->
                        viewModel.addPhoto(photoMeta)
                    }
                },
                onError = {
                    requireContext().showMessage("Permission denied")
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
