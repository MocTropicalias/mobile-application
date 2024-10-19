package com.tropicalias.api.model

import java.util.Date

data class Post(
    val id: Long,
    val userId: Long,
    val userImage: String?,
    val userName: String,
    val media: String?,
    val content: String,
    val likes: List<Long>,
    val coments: List<Comment>,
    val date: Date
)
