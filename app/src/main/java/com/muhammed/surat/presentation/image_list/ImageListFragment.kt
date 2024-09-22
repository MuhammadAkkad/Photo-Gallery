package com.muhammed.surat.presentation.image_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.muhammed.surat.R
import com.muhammed.surat.databinding.FragmentImageListBinding
import com.muhammed.surat.presentation.base.BaseFragment
import com.muhammed.surat.presentation.common.helper.CameraHelper
import com.muhammed.surat.presentation.image_list.adapter.PhotoAdapter
import com.muhammed.surat.util.collectFlow
import com.muhammed.surat.util.onClick
import com.muhammed.surat.util.showMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImageListFragment : BaseFragment<FragmentImageListBinding>() {

    @Inject
    lateinit var cameraHelper: CameraHelper
    private val viewModel: ImageListViewModel by viewModels()
    private lateinit var adapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = PhotoAdapter { photo ->
            val action =
                ImageListFragmentDirections.actionImageListFragmentToImageFragment(photo)
            navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initHeaderComponent()
        initObservers()
    }

    override fun initListeners() {
        with(binding) {
            btnTakePicture.onClick {
                cameraHelper.takePhoto(
                    onCaptured = {
                        it?.let { photoMeta ->
                            viewModel.addPhoto(photoMeta)
                        }
                    },
                    onError = {
                        context?.showMessage(getString(R.string.error_creating_photo))
                    }
                )
            }

            headerComponent.apply {
                with(viewModel) {
                    onFilter {
                        setFilter(it)
                    }
                    onSort {
                        sort(it)
                    }
                }
            }
        }
    }

    private fun initList() {
        binding.recyclerViewPhotos.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = this@ImageListFragment.adapter
        }
    }

    private fun initHeaderComponent() {
        binding.headerComponent.initState(
            viewModel.filterModel,
            viewModel.sortType
        )
    }

    private fun initObservers() {
        viewModel.photos.collectFlow {
            it?.let { photoList ->
                adapter.setItems(photoList)
            }
        }
    }
}
