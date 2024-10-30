package com.tropicalias.ui.search

import androidx.lifecycle.ViewModel
import com.tropicalias.adapter.PostAdapter
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.utils.ApiHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    lateinit var adapter: PostAdapter

    fun search(text: String, filterFollow: Boolean, filterLike: Boolean) {

        class PostResponseCallback(val adapter: PostAdapter) : Callback<List<Post>> {
            override fun onResponse(req: Call<List<Post>>, res: Response<List<Post>>) {
                res.body()?.let {
                    adapter.posts = res.body()!!
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(req: Call<List<Post>>, e: Throwable) {
                TODO("Not yet implemented")
            }
        }

        val noSqlApi = ApiRepository.getInstance().getNoSQL()

        ApiHelper.getUser { user ->
            when {
                filterLike -> {
                    noSqlApi.searchPosts(text, user.id, liked = true).enqueue(
                        PostResponseCallback(
                            adapter
                        )
                    )
                }

                filterFollow -> {

                    noSqlApi.searchPosts(text, user.id, following = true).enqueue(
                        PostResponseCallback(
                            adapter
                        )
                    )
                }

                else -> {
                    noSqlApi.searchPosts(text).enqueue(
                        PostResponseCallback(
                            adapter
                        )
                    )
                }
            }
        }

    }
}