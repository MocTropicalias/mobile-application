package com.tropicalias.ui.home.newpost

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tropicalias.R
import com.tropicalias.databinding.FragmentNewPostBinding
import com.tropicalias.databinding.ModalProfilePictureBinding
import com.tropicalias.utils.ImagePicker


class NewPostFragment : Fragment() {

    private var _binding: FragmentNewPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewPostViewModel by viewModels()

    var imageUri: Uri? = null
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val galleryImageUri = data?.data
                val finalUri = galleryImageUri ?: imageUri
                finalUri?.let {
                    imageUri = finalUri
                    viewModel.imageUrl = finalUri
                    Glide.with(binding.root.context)
                        .load(finalUri)
                        .into(binding.contentImageView)
                    binding.contentImageView.visibility = View.VISIBLE
                    binding.addImageTextView.visibility = View.GONE
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPostBinding.inflate(inflater, container, false)
        viewModel.binding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageImageButton.setOnClickListener {
            val bindingModal = ModalProfilePictureBinding.inflate(
                LayoutInflater.from(requireContext()),
                binding.root,
                false
            )

            val modal = BottomSheetDialog(requireContext(), R.style.SheetDialog)
            val parent = bindingModal.root.parent as? ViewGroup
            parent?.removeView(bindingModal.root)
            modal.setContentView(bindingModal.root)

            if (imageUri != null) {
                Glide.with(binding.root.context)
                    .load(imageUri)
                    .into(binding.contentImageView)

                binding.contentImageView.visibility = View.VISIBLE
            }

            imagePickerLauncher.let { ipl ->
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
                        .into(binding.contentImageView)
                    binding.contentImageView.visibility = View.GONE
                    binding.addImageTextView.visibility = View.VISIBLE
                    modal.dismiss()
                }
            }
        }

        binding.saveButton.setOnClickListener {
            val content = binding.contentEditText.text.toString()

            viewModel.savePost(content, imageUri)
        }


        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}