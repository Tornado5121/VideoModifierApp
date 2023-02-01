package com.example.videomodifyingapp.ui.videoListScreen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videomodifyingapp.base.BaseFragment
import com.example.videomodifyingapp.databinding.FragmentVideoListBinding
import com.example.videomodifyingapp.models.LocalStorageVideo
import com.example.videomodifyingapp.utils.flow_helper.collectOnLifecycle
import com.example.videomodifyingapp.utils.permissions.isPermissionsGranted
import org.koin.androidx.viewmodel.ext.android.viewModel

const val REQUEST_PERMISSION_CODE = 10

class VideoListFragment : BaseFragment<FragmentVideoListBinding>(
    FragmentVideoListBinding::inflate
) {

    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val videoListAdapter by lazy {
        VideoListAdapter { localStorageVideo ->
            navigateToPlayerViewerScreen(localStorageVideo)
        }
    }

    private val viewModel by viewModel<VideoListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
        askPermissions()
    }

    private fun setupView() {
        with(binding) {
            videoList.adapter = videoListAdapter
            videoList.layoutManager = LinearLayoutManager(requireContext())
            buttonRetryPermissions.setOnClickListener {
                askPermissions()
            }
        }
    }

    private fun setupObservers() {
        collectOnLifecycle(viewModel.screenState, collect = ::handleVideoListState)
    }

    private fun handleVideoListState(state: VideoListScreenState) {
        when (state) {
            VideoListScreenState.Error -> {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
                binding.progressBar.isVisible = false
            }
            VideoListScreenState.Progress -> binding.progressBar.isVisible = true
            is VideoListScreenState.Success -> {
                videoListAdapter.submitList(state.videoList)
                binding.progressBar.isVisible = false
            }
        }
    }

    private fun askPermissions() {
        if (isPermissionsGranted(
                permissions
            )
        ) {
            viewModel.loadVideoList()
        } else {
            requestPermissions(
                permissions, REQUEST_PERMISSION_CODE
            )
        }
    }

    private fun navigateToPlayerViewerScreen(localStorageVideo: LocalStorageVideo) {
        val action = VideoListFragmentDirections.actionVideoListFragmentToVideoViewerFragment(
            localStorageVideo
        )
        findNavController().navigate(action)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                viewModel.loadVideoList()
                binding.buttonRetryPermissions.isVisible = false
            } else {
                binding.buttonRetryPermissions.isVisible = true
            }
        }
    }
}