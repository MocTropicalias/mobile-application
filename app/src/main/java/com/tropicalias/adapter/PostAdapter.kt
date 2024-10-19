package com.tropicalias.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tropicalias.R
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.ItemPostBinding

class PostAdapter(var posts: List<Post>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = posts.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostViewHolder).bind(posts[position])
    }

    inner class PostViewHolder(val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post){
            Glide.with(binding.root.context)
                .load(post.userImage)
                .into(binding.image.profilePictureImageView)
            binding.image.imageTemplate.visibility = View.GONE

            if (post.media != null) {
                Glide.with(binding.root.context)
                    .load(post.media)
                    .into(binding.contentImageView)
            }

            binding.profileNameTextView.text = post.userName
            binding.contentTextView.text = post.content
            binding.dateTextView.text = DateFormat.format("dd/MM/yyyy", post.date)


            val noSqlApi = ApiRepository.getInstance().getNoSQL()

            var liked = post.likes.any { it == ApiRepository.getInstance().user.value?.id }
            binding.likePostImageButton.setImageResource(if (liked) R.drawable.ic_liked else R.drawable.ic_like)

            binding.likePostImageButton.setOnClickListener {
//                noSqlApi.likePost(post.id, ApiRepository.getInstance().user.value?.id!!).execute()
                liked = !liked
                binding.likePostImageButton.setImageResource(if (liked) R.drawable.ic_liked else R.drawable.ic_like)
            }

        }
    }

}