package com.tropicalias.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tropicalias.R
import com.tropicalias.databinding.ItemMascotBinding
import com.tropicalias.databinding.ItemProfilePictureBinding
import com.tropicalias.databinding.ItemSetMascotBinding
import com.tropicalias.databinding.ItemSetProfilePictureBinding
import com.tropicalias.databinding.ModalProfilePictureBinding
import com.tropicalias.utils.ApiHelper
import com.tropicalias.utils.ImagePicker


class ProfileAdapter(
    private val imagePickerLauncher: ActivityResultLauncher<Intent>?,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var imageUri: Uri? = null

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
                    val modalBinding = ModalProfilePictureBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                    SetProfileViewHolder(profileBinding, modalBinding)
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
            is SetProfileViewHolder -> holder.bind()
            is SetMascotViewHolder -> holder.bind()
            is ProfileViewHolder -> holder.bind()
            is MascotViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    // ViewHolder for Profile layout
    inner class SetProfileViewHolder(
        private val binding: ItemSetProfilePictureBinding,
        private val bindingModal: ModalProfilePictureBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            // Set the profile picture if the imageURL is available

            val modal = BottomSheetDialog(context, R.style.SheetDialog)
            val parent = bindingModal.root.parent as? ViewGroup
            parent?.removeView(bindingModal.root)
            modal.setContentView(bindingModal.root)

            if (imageUri != null) {
                Glide.with(binding.root.context)
                    .load(imageUri)
                    .into(binding.profilePictureImageView)

                binding.imageTemplate.visibility = View.GONE
            }

            imagePickerLauncher?.let { ipl ->
                binding.root.setOnClickListener {
                    modal.show()
                    bindingModal.chosePictureButton.setOnClickListener {
                        val (chooserIntent, uri) = ImagePicker.getChoserIntent(binding.root.context)
                        imageUri = uri
                        ipl.launch(chooserIntent)
                        modal.dismiss()
                    }
                    bindingModal.removePictureButton.setOnClickListener {
                        imageUri = null
                        Glide.with(binding.root.context)
                            .load("")
                            .into(binding.profilePictureImageView)
                        binding.imageTemplate.visibility = View.VISIBLE
                        modal.dismiss()
                    }
                }
            }
        }
    }

    inner class ProfileViewHolder(private val binding: ItemProfilePictureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            // Set the profile picture if the imageURL is available
            if (imageUri != null) {
                Glide.with(binding.root.context)
                    .load(imageUri)
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
