package com.example.videomodifyingapp.ui.videoViewerScreen

import android.net.Uri
import android.os.Bundle
import android.view.View
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
        collectOnLifecycle(viewModel.reversedVideoUri, collect = ::handleMediaItemType)
    }

    private fun handleMediaItemType(videoUri: String) {
        myPlayer.playVideoByUri(Uri.parse(videoUri))
    }

    private fun setupView() {
        with(binding) {
            player.player = myPlayer.getPlayerInstance()

            buttonPlayRegularVideo.setOnClickListener {
                myPlayer.playVideoByUri(args.video.uri)
            }
            buttonPlayReversedVideo.setOnClickListener {
                viewModel.reverseVideoByTwoParts(args.video.uri, args.video.duration)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        myPlayer.clearResources()
    }
}