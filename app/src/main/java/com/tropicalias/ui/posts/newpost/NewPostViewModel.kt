package com.tropicalias.ui.posts.newpost

import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModel
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentNewPostBinding
import com.tropicalias.utils.ApiHelper

class NewPostViewModel : ViewModel() {

    var imageUrl: Uri? = null
    private val noSQLApi = ApiRepository.getInstance().getNoSQL()
    lateinit var binding: FragmentNewPostBinding
    private var posting = false

    fun savePost(content: String, imageUri: Uri?, callback: () -> Unit) {
        if (!posting) {
            posting = true
            binding.saveButton.text = ""
            binding.loadingButton.visibility = View.VISIBLE

            ApiHelper.getUser {
                val post = Post(
                    userId = it.id!!,
                    userImage = it.imageUri,
                    userName = it.exibitionName ?: it.username,
                    media = imageUri,
                    content = content,
                )

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

                    }
                })
            }
        }

    }
}