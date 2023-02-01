package com.example.videomodifyingapp.ui.videoListScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.videomodifyingapp.databinding.ItemVideoBinding
import com.example.videomodifyingapp.models.LocalStorageVideo

class VideoListAdapter(
    private val onClick: (LocalStorageVideo) -> Unit
) :
    ListAdapter<LocalStorageVideo, VideoListAdapter.VideoViewHolder>(VideoDiffUtils()) {

    class VideoViewHolder(
        private val binding: ItemVideoBinding,

        ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            data: LocalStorageVideo,
            onClick: (LocalStorageVideo) -> Unit
        ) {
            with(binding) {
                name.text = data.name
                root.setOnClickListener {
                    onClick(data)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): VideoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemVideoBinding.inflate(layoutInflater, parent, false)
                return VideoViewHolder(binding)
            }
        }

    }

    class VideoDiffUtils : DiffUtil.ItemCallback<LocalStorageVideo>() {
        override fun areItemsTheSame(
            oldItem: LocalStorageVideo,
            newItem: LocalStorageVideo
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: LocalStorageVideo,
            newItem: LocalStorageVideo
        ): Boolean {
            return oldItem.name == newItem.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(currentList[position], onClick)
    }
}