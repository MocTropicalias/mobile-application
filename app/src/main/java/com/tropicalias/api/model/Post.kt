package com.tropicalias.api.model

import android.net.Uri
import java.util.Date

data class Post(
    val id: Long,
    val userId: Long,
    val userImage: Uri?,
    val userName: String,
    val media: Uri?,
    val content: String,
    val likes: List<Long>,
    val coments: List<Comment>,
    val date: Date
)
