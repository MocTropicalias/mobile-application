package com.tropicalias.api.model

data class PostPage(
    val posts: List<Post>,
    val page: Int,
    val numberOfPosts: Int,
)
