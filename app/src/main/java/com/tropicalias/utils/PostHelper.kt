package com.tropicalias.utils

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.bumptech.glide.Glide
import com.tropicalias.R
import com.tropicalias.api.Retrofit.ApiType.LANDINGPAGE
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.ActivityPostDetailsBinding
import com.tropicalias.databinding.ItemPostBinding
import com.tropicalias.databinding.ItemProfilePictureBinding
import com.tropicalias.ui.posts.postdetails.PostDetailsActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

enum class BindingType {
    MAIN_ACTIVITY,
    POST_ACTIVITY
}

interface PostBinding {
    val root: View
    val profileNameTextView: TextView
    val likePostImageButton: ImageButton
    val image: ItemProfilePictureBinding
    val nav: NavController?
}

fun ItemPostBinding.toPostBinding(navController: NavController?): PostBinding =
    object : PostBinding {
    override val root = this@toPostBinding.root
    override val profileNameTextView = this@toPostBinding.profileNameTextView
    override val likePostImageButton = this@toPostBinding.likePostImageButton
    override val image = this@toPostBinding.image
        override val nav = navController
}

fun ActivityPostDetailsBinding.toPostBinding(navController: NavController?): PostBinding =
    object : PostBinding {
    override val root = this@toPostBinding.root
    override val profileNameTextView = this@toPostBinding.profilePostNameTextView
    override val likePostImageButton = this@toPostBinding.likePostFullImageButton
    override val image = this@toPostBinding.image
        override val nav = navController
}

class PostHelper<T : PostBinding>(private val binding: T) {

    fun openPost(
        postId: String,
        activityResultLauncher: ActivityResultLauncher<Intent>?,
        comment: Boolean
    ) {
        val intent = Intent(binding.root.context, PostDetailsActivity::class.java)

        val uri = Uri.parse("tropicalias://post/?postId=$postId")
        intent.data = uri
        Log.d("PostAdapter", "opening post with id: $postId")
        intent.putExtra("comment", comment)

        if (activityResultLauncher != null) {
            activityResultLauncher.launch(intent)
        } else {
            startActivity(binding.root.context, intent, null)
        }
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
                "Check out this post: ${LANDINGPAGE.url}/post/?postId=$postId"
            ) //${dynamicLink.uri}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        binding.root.context.startActivity(shareIntent)
    }

    fun openProfile(userId: Long) {
        val bundle = Bundle().apply {
            putLong("userId", userId)
        }
        if (binding.nav != null) {
            binding.nav!!.navigate(
                R.id.action_navigation_home_to_navigation_profile,
                bundle,
                NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_home, true)
                    .build()
            )
        }
    }
}