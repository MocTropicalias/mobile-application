package com.tropicalias.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tropicalias.databinding.ItemMascotBinding
import com.tropicalias.databinding.ItemProfilePictureBinding

class ProfileAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        private const val VIEW_TYPE_PROFILE = 0
        private const val VIEW_TYPE_MASCOT = 1
    }

    var imageURL: Uri? = null
        set(value) {
            field = value
            notifyDataSetChanged() // Update all items when image changes
        }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) VIEW_TYPE_MASCOT else VIEW_TYPE_PROFILE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PROFILE -> {
                val profileBinding = ItemProfilePictureBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ProfileViewHolder(profileBinding)
            }

            VIEW_TYPE_MASCOT -> {
                val mascotBinding =
                    ItemMascotBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                MascotViewHolder(mascotBinding)
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProfileViewHolder -> holder.bind(imageURL) // Pass the current image URL to bind
            is MascotViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    // ViewHolder for Profile layout
    inner class ProfileViewHolder(private val binding: ItemProfilePictureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageURL: Uri?) {
            Log.d("TAGProfileAdapter", "bind: $imageURL")
            if (imageURL != null) {
                binding.profilePictureImageView.setImageURI(imageURL)
                binding.imageTemplate.visibility = View.GONE
            } else {
                binding.imageTemplate.visibility = View.VISIBLE
            }
        }
    }

    // ViewHolder for Mascot layout
    inner class MascotViewHolder(private val binding: ItemMascotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
        }
    }
}
