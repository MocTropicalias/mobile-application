package com.tropicalias.utils

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation.findNavController
import com.bumptech.glide.Glide
import com.tropicalias.R
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.ActivityPostDetailsBinding
import com.tropicalias.databinding.ItemPostBinding
import com.tropicalias.databinding.ItemProfilePictureBinding
import com.tropicalias.ui.posts.postdetails.PostDetailsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

interface PostBinding {
    val root: View
    val profileNameTextView: TextView
    val likePostImageButton: ImageButton
    val image: ItemProfilePictureBinding
}

fun ItemPostBinding.toPostBinding(): PostBinding = object : PostBinding {
    override val root = this@toPostBinding.root
    override val profileNameTextView = this@toPostBinding.profileNameTextView
    override val likePostImageButton = this@toPostBinding.likePostImageButton
    override val image = this@toPostBinding.image
}

fun ActivityPostDetailsBinding.toPostBinding(): PostBinding = object : PostBinding {
    override val root = this@toPostBinding.root
    override val profileNameTextView = this@toPostBinding.profilePostNameTextView
    override val likePostImageButton = this@toPostBinding.likePostFullImageButton
    override val image = this@toPostBinding.image
}

class PostHelper<T : PostBinding>(private val binding: T) {

    fun openPost(postId: String) {
        val intent = Intent(binding.root.context, PostDetailsActivity::class.java)

        val uri = Uri.parse("tropicalias://post/$postId")
        intent.data = uri

        startActivity(binding.root.context, intent, null)
    }

    fun loadUser(post: Post) {
        if (post.userPhoto != null) {
            Glide.with(binding.root.context)
                .load(post.userPhoto)
                .into(binding.image.profilePictureImageView)
            binding.image.imageTemplate.visibility = View.GONE
        }
        binding.profileNameTextView.text = post.userName

        ApiHelper.loadProfile(post.userId) {
            binding.profileNameTextView.text = it.exibitionName ?: it.username
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
        val formatedDate = DateFormat.format("dd/MM/yyyy", date).toString()
//        Log.d("PostAdapter", "Date formated as: $formatedDate")
        return formatedDate
    }

    fun like(liked: Boolean, post: Post) {
        ApiHelper.getUser {
            ApiRepository.getInstance().getNoSQL().toggleLikePost(post.id!!, it.id!!)
                .enqueue(object :
                    Callback<Int> {
                    override fun onResponse(req: Call<Int>, res: Response<Int>) {
                        Log.d("PostAdapter", "Quantidades de likes no post: ${res.body()}")
                    }

                    override fun onFailure(req: Call<Int>, e: Throwable) {
                        Log.d("PostAdapter", "Erro ao dar like: ${e.message}")
                    }

                })
        }
        binding.likePostImageButton.setImageResource(if (liked) R.drawable.ic_liked else R.drawable.ic_like)
        if (liked) {
            DrawableHandler.likeAnimation(binding.likePostImageButton)
        }
    }

    fun share(postId: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this post: https://landing-page-lw54.onrender.com/post/$postId"
            ) //${dynamicLink.uri}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        binding.root.context.startActivity(shareIntent)
    }

    fun openProfile(userId: Long) {
        val navController = findNavController(binding.root)
        val bundle = Bundle().apply {
            putLong("userId", userId)
        }
        navController.navigate(R.id.navigation_profile, bundle)
    }
}