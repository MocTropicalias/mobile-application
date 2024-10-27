package com.tropicalias.ui.posts.postdetails

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.tropicalias.R
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.ActivityPostDetailsBinding
import com.tropicalias.utils.PostHelper
import com.tropicalias.utils.toPostBinding

class PostDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityPostDetailsBinding
    val viewModel: PostDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.fecharPostImageView.setOnClickListener {
            onBackPressed()
        }

        val postId = intent.data?.lastPathSegment

        if (postId == null) {
            finish()
        }
        viewModel.loadPost(postId!!) { post ->
            loadPost(post)
        }

    }

    private fun loadPost(post: Post) {
        val postHelper = PostHelper(binding.toPostBinding())
//        postHelper.loadUser(post)

        // Post content
        if (post.media != null) {
            Glide.with(binding.root.context)
                .load(post.media)
                .into(binding.contentPostImageView)
            binding.contentPostImageView.visibility = View.VISIBLE
        }

        binding.contentPostTextView.text = post.content
        binding.datePostTextView.text = post.createdAt?.let { postHelper.dateFormat(it) }

        // Post Likes
        var liked = post.likes.any { it == ApiRepository.getInstance().user.value?.id }
        binding.likePostFullImageButton.setImageResource(if (liked) R.drawable.ic_liked else R.drawable.ic_like)
        binding.likePostFullImageButton.setOnClickListener {
            liked = !liked
            postHelper.like(liked, post)
        }

        // Post Share
        binding.sharePostImageButton.setOnClickListener {
            post.id?.let { id -> postHelper.share(id) }
        }

        // Post Open User
        binding.profilePostImageView.setOnClickListener { postHelper.openProfile(post.userId) }
        binding.profilePostNameTextView.setOnClickListener { postHelper.openProfile(post.userId) }


        // Post Open Image
        binding.contentPostImageView
    }
}