package com.tropicalias.ui.posts.postdetails

import androidx.lifecycle.ViewModel
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailsViewModel : ViewModel() {

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

}