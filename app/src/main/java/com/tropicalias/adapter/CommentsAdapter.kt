package com.tropicalias.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tropicalias.api.model.Comment
import com.tropicalias.databinding.ItemCommentBinding
import com.tropicalias.utils.ApiHelper
import com.tropicalias.utils.PostHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class CommentsAdapter(var comments: List<Comment>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CommentViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = comments.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("CommentsAdapter", "onBindViewHolder: ${comments[position]}")
        (holder as CommentViewHolder).bind(comments[position])
    }

    inner class CommentViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            if (comment.userImage != null) {
                Glide.with(binding.root.context)
                    .load(comment.userImage)
                    .into(binding.image.profilePictureImageView)
                binding.image.imageTemplate.visibility = View.GONE
            }

            binding.profileNameCommentTextView.text = comment.userName
            binding.commentContentTextView.text = comment.content
            binding.dateTextView.text = comment.date?.let { dateFormat(it) }

            //Loading profile
            ApiHelper.loadProfile(comment.userId) {
                binding.profileNameCommentTextView.text = it.exibitionName
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

        fun dateFormat(date: Date): String {
            val now = Date()
            val diffInMillis = now.time - date.time

            return when {
                diffInMillis < TimeUnit.HOURS.toMillis(1) -> {
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
                    "$minutes minutos atrás"
                }

                diffInMillis < TimeUnit.DAYS.toMillis(1) -> {
                    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
                    "$hours horas atrás"
                }

                diffInMillis < TimeUnit.DAYS.toMillis(7) -> {
                    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                    "$days dias atrás"
                }

                else -> {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
                    dateFormat.format(date)
                }
            }
        }


    }

}