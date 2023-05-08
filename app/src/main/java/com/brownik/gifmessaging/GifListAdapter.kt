package com.brownik.gifmessaging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brownik.gifmessaging.databinding.ItemGifBinding
import com.giphy.sdk.core.models.Media

class GifListAdapter : ListAdapter<Media, GifListAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Media>() {
            override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Media,
                newItem: Media,
            ): Boolean {
                return true
            }
        }
    }

    inner class ViewHolder(private val binding: ItemGifBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(media: Media) = with(binding) {
            container.addView(MakeView.makeGPHMediaView(container.context, media))
            container.addView(MakeView.makeImageView(container.context, media))
            container.addView(MakeView.makeSimpleDraweeView(container.context, media))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGifBinding.bind(
            layoutInflater.inflate(
                R.layout.item_gif, parent, false
            )
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}