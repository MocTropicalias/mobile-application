package com.tropicalias.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tropicalias.R
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.ItemPostBinding
import com.tropicalias.ui.posts.image.FullscreenImageActivity
import com.tropicalias.utils.PostHelper
import com.tropicalias.utils.toPostBinding

class PostAdapter(
    var posts: List<Post>,
    val navController: NavController? = null,
    val activityResultLauncher: ActivityResultLauncher<Intent>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = posts.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostViewHolder).bind(posts[position])
    }

    inner class PostViewHolder(val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post){
            val postHelper = PostHelper(binding.toPostBinding(navController))

            // Post user
            postHelper.loadUser(post)

            // Post content
            if (post.media != null) {
                Log.d("PostAdapter", "Carregando Foto do post ${post.id}: ${post.media}")
                Glide.with(binding.root.context)
                    .load(post.media)
                    .fitCenter()
                    .into(binding.contentImageView as ImageView)
                binding.contentImageView.visibility = View.VISIBLE
            } else {
                binding.contentImageView.visibility = View.GONE
            }


            binding.contentTextView.text = post.content
            binding.dateTextView.text = post.createdAt?.let { postHelper.dateFormat(it) }

            // Post Likes
            var liked = post.likes.any { it == ApiRepository.getInstance().user.value?.id }
            binding.likePostImageButton.setImageResource(if (liked) R.drawable.ic_liked else R.drawable.ic_like)
            binding.likePostImageButton.setOnClickListener {
                liked = !liked
                postHelper.like(liked, post)
            }

            // Post Share
            binding.shareImageButton.setOnClickListener {
                post.id?.let { id -> postHelper.share(id) }
            }

            // Post Open User
            binding.profileImageView.setOnClickListener { postHelper.openProfile(post.userId) }
            binding.profileNameTextView.setOnClickListener { postHelper.openProfile(post.userId) }


            // Post Details
            post.id?.let { id ->
                Log.d("PostAdapter", "post details on click: $id")
                binding.root.setOnClickListener {
                    postHelper.openPost(
                        id,
                        activityResultLauncher,
                        false
                    )
                }
                binding.commentsImageButton.setOnClickListener {
                    postHelper.openPost(
                        id,
                        activityResultLauncher,
                        true
                    )
                }
            }

            // Post Open Image
            binding.contentImageView.setOnClickListener {
                val intent = Intent(binding.root.context, FullscreenImageActivity::class.java)
                intent.putExtra("IMAGE_URL", post.media)
                startActivity(binding.root.context, intent, null)
            }

        }

    }


}