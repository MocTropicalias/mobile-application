package com.tropicalias.ui.search

import android.view.View
import androidx.lifecycle.ViewModel
import com.tropicalias.adapter.PostAdapter
import com.tropicalias.api.model.Post
import com.tropicalias.api.repository.ApiRepository
import com.tropicalias.databinding.FragmentSearchBinding
import com.tropicalias.utils.ApiHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    lateinit var adapter: PostAdapter
    lateinit var binding: FragmentSearchBinding

    fun search(text: String, filterFollow: Boolean, filterLike: Boolean) {

        class PostResponseCallback(val adapter: PostAdapter) : Callback<List<Post>> {
            override fun onResponse(req: Call<List<Post>>, res: Response<List<Post>>) {
                adapter.posts = emptyList()
                adapter.notifyDataSetChanged()
                binding.loading.visibility = View.GONE
                res.body()?.let {
                    adapter.posts = res.body()!!
                    adapter.notifyDataSetChanged()
                }
                if (adapter.posts.isEmpty()) {
                    binding.notFoundLayout.visibility = View.VISIBLE
                }
            }

            override fun onFailure(req: Call<List<Post>>, e: Throwable) {
                GlobalScope.launch {
                    delay(20000)
                    search(text, filterFollow, filterLike)
                }
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