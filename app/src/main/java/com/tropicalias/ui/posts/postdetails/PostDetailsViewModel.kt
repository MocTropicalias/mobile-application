package com.tropicalias.ui.posts.postdetails

import androidx.lifecycle.ViewModel
import com.tropicalias.api.model.Comment
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.utils.ApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailsViewModel : ViewModel() {

    lateinit var post: Post

    fun loadPost(postId: String, callback: (Post) -> Unit) {
        ApiRepository.getInstance().getNoSQL().getPostById(postId)
            .enqueue(object : Callback<Post> {
                override fun onResponse(req: Call<Post>, res: Response<Post>) {
                    res.body()?.let { callback(it) }
                }

                override fun onFailure(req: Call<Post>, e: Throwable) {
                    GlobalScope.launch(Dispatchers.Main) {
                        delay(30000)
                        loadPost(postId, callback)
                    }
                }

            })
    }

    private var sendingComment: Boolean = false
    fun sendComment(text: String, callback: () -> Unit) {
        if (!sendingComment) {
            sendingComment = true
            ApiHelper.getUser { user ->
                val comment = Comment(
                    user.id!!,
                    user.imageUri,
                    user.exibitionName ?: user.username,
                    text,
                    null
                )
                ApiRepository.getInstance().getNoSQL().addComment(post.id!!, comment)
                    .enqueue(object : Callback<Unit> {
                        override fun onResponse(req: Call<Unit>, res: Response<Unit>) {
                            callback()
                        }

                        override fun onFailure(req: Call<Unit>, e: Throwable) {

                        }

                    })
            }
        }
    }

}