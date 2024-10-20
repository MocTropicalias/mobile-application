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
) {
    constructor(
        userId: Long,
        userImage: Uri?,
        userName: String,
        media: Uri?,
        content: String,
    ) : this(
        0,
        userId,
        userImage,
        userName,
        media,
        content,
        emptyList(),
        emptyList(),
        Date()
    )
}
