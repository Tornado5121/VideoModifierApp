package com.example.videomodifyingapp.ui.videoViewerScreen

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.videomodifyingapp.base.BaseFragment
import com.example.videomodifyingapp.databinding.FragmentVideoViewerBinding
import com.example.videomodifyingapp.utils.flow_helper.collectOnLifecycle
import com.example.videomodifyingapp.utils.my_player.MyPlayer
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideoViewerFragment : BaseFragment<FragmentVideoViewerBinding>(
    FragmentVideoViewerBinding::inflate
) {

    private val args by navArgs<VideoViewerFragmentArgs>()
    private val myPlayer by lazy { MyPlayer(requireContext()) }
    private val viewModel by viewModel<VideoViewerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
    }

    private fun setupObserver() {
        collectOnLifecycle(viewModel.state, collect = ::handleScreenState)
    }

    private fun handleScreenState(state: VideoViewerScreenState?) {
        when (state) {
            is VideoViewerScreenState.Error -> {
                binding.progressBar.isVisible = false
                Toast.makeText(
                    requireContext(),
                    state.errorMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
            is VideoViewerScreenState.ProcessVideo -> {
                myPlayer.setVideoSource(Uri.parse(state.videoUri))
                binding.progressBar.isVisible = true
            }
            VideoViewerScreenState.Success -> {
                binding.progressBar.isVisible = false
                myPlayer.play()
            }
            VideoViewerScreenState.Idle -> {
                binding.progressBar.isVisible = false
            }
            null -> {}
        }
    }

    private fun setupView() {
        with(binding) {
            player.player = myPlayer.getPlayerInstance()

            buttonPlayRegularVideo.setOnClickListener {
                myPlayer.setVideoSource(args.video.uri)
                myPlayer.play()
            }
            buttonPlayReversedVideo.setOnClickListener {
                args.video.duration?.let { duration ->
                    viewModel.reverseVideoByTwoParts(
                        args.video.uri,
                        duration,
                        requireContext().contentResolver
                    )
                }
            }
        }
    }

    override fun onStop() {
        myPlayer.clearResources()
        viewModel.clearFolder()
        super.onStop()
    }
}