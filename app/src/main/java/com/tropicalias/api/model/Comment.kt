package com.tropicalias.api.model

data class Comment(
    val userId: Long,
    val userImage: String,
    val userName: String,
    val content: String
)
