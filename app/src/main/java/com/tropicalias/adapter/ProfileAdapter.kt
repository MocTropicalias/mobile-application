package com.tropicalias.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tropicalias.databinding.ItemMascotBinding
import com.tropicalias.databinding.ItemProfilePictureBinding
import com.tropicalias.databinding.ItemSetMascotBinding
import com.tropicalias.databinding.ItemSetProfilePictureBinding
import com.tropicalias.utils.Utils

class ProfileAdapter(private val imagePickerLauncher: ActivityResultLauncher<Intent>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var imageUrl: Uri? = null

    companion object {
        private const val VIEW_TYPE_PROFILE = 0
        private const val VIEW_TYPE_MASCOT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) VIEW_TYPE_MASCOT else VIEW_TYPE_PROFILE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_PROFILE -> {
                if (imagePickerLauncher != null) {
                    val profileBinding = ItemSetProfilePictureBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                    SetProfileViewHolder(profileBinding)
                } else {
                    val profileBinding = ItemProfilePictureBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                    ProfileViewHolder(profileBinding)
                }
            }

            VIEW_TYPE_MASCOT -> {
                if (imagePickerLauncher != null) {
                    val mascotBinding = ItemSetMascotBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                    SetMascotViewHolder(mascotBinding)
                } else {
                    val mascotBinding = ItemMascotBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                    MascotViewHolder(mascotBinding)
                }
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SetProfileViewHolder -> holder.bind(imageUrl) // Pass the current image URL to bind
            is SetMascotViewHolder -> holder.bind()
            is ProfileViewHolder -> holder.bind(imageUrl)
            is MascotViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    // ViewHolder for Profile layout
    inner class SetProfileViewHolder(private val binding: ItemSetProfilePictureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageURL: Uri?) {
            // Set the profile picture if the imageURL is available
            if (imageURL != null) {
                Glide.with(binding.root.context)
                    .load(imageURL)
                    .into(binding.profilePictureImageView)

                binding.imageTemplate.visibility = View.GONE
            }

            imagePickerLauncher?.let { ipl ->
                binding.root.setOnClickListener {
                    Log.d("TAGProfileAdapter", "Profile image clicked: ")
                    val (chooserIntent, uri) = Utils.getChoserIntent(binding.root.context)
                    imageUrl = uri
                    ipl.launch(chooserIntent)
                }
            }
        }
    }

    inner class ProfileViewHolder(private val binding: ItemProfilePictureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageURL: Uri?) {
            // Set the profile picture if the imageURL is available
            if (imageURL != null) {
                Glide.with(binding.root.context)
                    .load(imageURL)
                    .into(binding.profilePictureImageView)

                binding.imageTemplate.visibility = View.GONE
            }
        }
    }

    // ViewHolder for Mascot layout
    inner class SetMascotViewHolder(private val binding: ItemSetMascotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {
                // Open Mascot color selection screen
            }
        }
    }

    inner class MascotViewHolder(private val binding: ItemMascotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {
                // Open Mascot color selection screen
            }
        }
    }
}
