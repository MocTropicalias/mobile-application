package com.tropicalias.ui.posts.newpost

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentNewPostBinding
import com.tropicalias.utils.ApiHelper
import java.util.UUID

class NewPostViewModel : ViewModel() {

    var imageUrl: Uri? = null
    private val noSQLApi = ApiRepository.getInstance().getNoSQL()
    lateinit var binding: FragmentNewPostBinding
    private var posting = false

    fun savePost(content: String, imageUri: Uri?, callback: () -> Unit) {
        if (posting) {
            Log.d("NewPostViewModel", "Post is already in progress, skipping duplicate request.")
            return
        }
        posting = true
        binding.saveButton.text = ""
        binding.loadingButton.visibility = View.VISIBLE
        binding.saveButton.isEnabled = false  // Disable button immediately

        ApiHelper.getUser { user ->
            if (imageUri != null) {
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference.child("post/${UUID.randomUUID()}.jpg")
                storageRef.putFile(imageUri)
                    .addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener { imageUri ->
                            val post = Post(
                                userId = user.id!!,
                                userImage = user.imageUri,
                                userName = user.exibitionName ?: user.username,
                                media = imageUri,
                                content = content,
                            )
                            Log.d("NewPostViewModel", "Creating post with image URI: $imageUri")
                            createPost(post, callback)
                        }
                    }
                    .addOnFailureListener {
                        posting = false  // Reset posting flag on failure
                        binding.saveButton.isEnabled = true
                    }
            } else {
                val post = Post(
                    userId = user.id!!,
                    userImage = user.imageUri,
                    userName = user.exibitionName ?: user.username,
                    media = null,
                    content = content,
                )
                Log.d("NewPostViewModel", "Creating post without image.")
                createPost(post, callback)
            }
        }
    }


    fun createPost(post: Post, callback: () -> Unit) {
        noSQLApi.createPost(post).enqueue(object : retrofit2.Callback<Post> {
            override fun onResponse(
                call: retrofit2.Call<Post>,
                response: retrofit2.Response<Post>
            ) {
                if (response.isSuccessful) {
                    Log.d("NewPostViewModel", "Post created successfully.")
                    imageUrl = null
                }
                posting = false
                binding.saveButton.isEnabled = true
                callback()
            }

            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                Log.e("NewPostViewModel", "Post creation failed: ${t.message}")
                posting = false  // Reset posting flag on failure
                binding.saveButton.isEnabled = true
                // Optionally show error message to user instead of retrying
            }
        })
    }


}