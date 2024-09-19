package com.tropicalias.api.model

data class Post(
    val id: Long,
    val userId: Long,
    val userPhoto: String,
    val userName: String,
    val media: String,
    val title: String?,
    val content: String,
    val likes: Int,
    val coments: List<Comment>
)
