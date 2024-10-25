package com.tropicalias.ui.posts.newpost

import android.net.Uri
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
            return
        }
        posting = true
        binding.saveButton.text = ""
        binding.loadingButton.visibility = View.VISIBLE

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

                            createPost(post, callback)
                        }
                    }
                    .addOnFailureListener {}
            } else {
                val post = Post(
                    userId = user.id!!,
                    userImage = user.imageUri,
                    userName = user.exibitionName ?: user.username,
                    media = null,
                    content = content,
                )

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
                    imageUrl = null
                }
                callback()
            }

            override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                createPost(post, callback)
            }
        })
    }

}