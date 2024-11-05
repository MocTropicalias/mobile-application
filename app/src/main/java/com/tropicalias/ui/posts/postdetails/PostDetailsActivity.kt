package com.tropicalias.ui.posts.postdetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tropicalias.R
import com.tropicalias.adapter.CommentsAdapter
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.ActivityPostDetailsBinding
import com.tropicalias.databinding.ModalCommentBinding
import com.tropicalias.ui.posts.image.FullscreenImageActivity
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

        val postId = intent.data?.getQueryParameter("postId")
        val comment = intent.extras?.getBoolean("comment")

        if (postId == null) {
            finish()
        }
        if (postId != null) {
            binding.loading.visibility = View.VISIBLE
            viewModel.loadPost(postId) { post ->
                loadPost(post, comment)
                binding.loading.visibility = View.GONE
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (postId != null) {
                binding.loading.visibility = View.VISIBLE
                viewModel.loadPost(postId) { post ->
                    loadPost(post, comment)
                }
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }

    }

    private fun loadPost(post: Post, comment: Boolean?) {
        Log.d("PostDetailsActivity", "loadPost: $post")
        val postHelper = PostHelper(binding.toPostBinding(null))
        postHelper.loadUser(post)

        viewModel.post = post

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
        binding.likePostTextView.text = post.likes.size.toString()
        val likesAmount = post.likes.size
        binding.commentsTextView.text = post.comments.size.toString()
        binding.likePostImageButton.setImageResource(if (liked) R.drawable.ic_liked else R.drawable.ic_like)
        binding.likePostImageButton.setOnClickListener {
            liked = !liked
            postHelper.like(liked, post)
        }

        // Post Share
        binding.shareImageButton.setOnClickListener {
            post.id?.let { id -> postHelper.share(id) }
        }

        // In PostDetailsActivity
        fun openProfile(userId: Long, postId: String) {
            val intent = Intent().apply {
                putExtra("navigateToProfile", true)
                putExtra("userId", userId)
                putExtra("postId", postId)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        // Post Open User
        post.id?.let {
            binding.profilePostImageView.setOnClickListener { openProfile(post.userId, post.id) }
            binding.profilePostNameTextView.setOnClickListener { openProfile(post.userId, post.id) }
        }


        // Post Open Image
        binding.contentPostImageView.setOnClickListener {
            val intent = Intent(binding.root.context, FullscreenImageActivity::class.java)
            intent.putExtra("IMAGE_URL", post.media)
            startActivity(intent)
        }

        if (comment == true) {
            openModal()
        }
        binding.commentsImageButton.setOnClickListener {
            openModal()
        }

        val adapter = CommentsAdapter(post.comments)
        binding.comentariosRecyclerView.adapter = adapter
        binding.comentariosRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }


    private fun openModal() {
        val bindingModal = ModalCommentBinding.inflate(layoutInflater)
        val modal = BottomSheetDialog(this, R.style.SheetDialog)
        val parent = bindingModal.root.parent as? ViewGroup
        parent?.removeView(bindingModal.root)
        modal.setContentView(bindingModal.root)
        val params: WindowManager.LayoutParams = modal.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        modal.window!!.setAttributes(params)
        modal.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        modal.show()

        bindingModal.inputMessage.requestFocus()

        bindingModal.layoutSend.setOnClickListener {
            val text = bindingModal.inputMessage.text.toString()
            bindingModal.layoutSend.visibility = View.INVISIBLE
            bindingModal.loading.visibility = View.VISIBLE
            viewModel.sendComment(text) {
                modal.dismiss()
            }
        }
    }


}