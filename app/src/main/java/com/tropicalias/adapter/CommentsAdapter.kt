package com.tropicalias.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tropicalias.api.model.Comment
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.ItemPostBinding
import com.tropicalias.utils.ApiHelper

class CommentsAdapter(var comments: List<Comment>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = comments.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostViewHolder).bind(comments[position])
    }

    inner class PostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            if (comment.userImage != null) {
                Glide.with(binding.root.context)
                    .load(comment.userImage)
                    .into(binding.image.profilePictureImageView)
                binding.image.imageTemplate.visibility = View.GONE
            }

            binding.profileNameTextView.text = comment.userName
            binding.contentTextView.text = comment.content
            binding.dateTextView.text = DateFormat.format("dd/MM/yyyy", comment.date)

            val noSqlApi = ApiRepository.getInstance().getNoSQL()

            //Loading profile
            ApiHelper.loadProfile(comment.userId) {
                binding.profileNameTextView.text = it.exibitionName
                if (it.imageUri != null) {
                    Glide.with(binding.root.context)
                        .load(it.imageUri)
                        .into(binding.image.profilePictureImageView)
                    binding.image.imageTemplate.visibility = View.GONE
                } else {
                    binding.image.imageTemplate.visibility = View.VISIBLE
                    Glide.with(binding.root.context)
                        .load("")
                        .into(binding.image.profilePictureImageView)
                }
            }


        }
    }

}